const API_URL = "http://localhost:8080/api";

// --------- Navegación ---------
const sections = ["users-section", "meds-section", "doses-section"];
document.querySelectorAll("#main-menu button").forEach(btn => {
  btn.onclick = function() {
    sections.forEach(s => document.getElementById(s).classList.add("hidden"));
    document.getElementById(this.dataset.section).classList.remove("hidden");
    document.querySelectorAll("#main-menu button").forEach(b => b.classList.remove("active"));
    this.classList.add("active");
    // Recargar datos según sección
    if (this.dataset.section === "users-section") loadUsers();
    if (this.dataset.section === "meds-section") loadMeds();
    if (this.dataset.section === "doses-section") {
      loadDoseUsers();
      loadDoseMeds();
      loadDoses();
      loadDoseHistory();
    }
  };
});
document.querySelector("#main-menu button").click(); // Por defecto

// --------- CRUD Usuarios ---------
async function loadUsers() {
  const tbody = document.getElementById("users-table");
  tbody.innerHTML = "";
  try {
    const res = await fetch(`${API_URL}/users`);
    const users = await res.json();
    users.forEach(u => {
      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td>${u.nombre}</td>
        <td>${u.email}</td>
        <td>
          <button class="action-btn" onclick="editUser('${u.id}','${u.nombre}','${u.email}')">Editar</button>
          <button class="action-btn delete" onclick="deleteUser('${u.id}')">Eliminar</button>
        </td>`;
      tbody.appendChild(tr);
    });
  } catch {}
}
window.editUser = (id, nombre, email) => {
  document.getElementById("user-id").value = id;
  document.getElementById("user-name").value = nombre;
  document.getElementById("user-email").value = email;
  document.getElementById("user-save-btn").textContent = "Actualizar usuario";
  document.getElementById("user-cancel-btn").classList.remove("hidden");
};
window.deleteUser = async id => {
  if (!confirm("¿Eliminar usuario?")) return;
  await fetch(`${API_URL}/users/${id}`, { method: "DELETE" });
  loadUsers();
};
document.getElementById("user-form").onsubmit = async function(e) {
  e.preventDefault();
  const id = document.getElementById("user-id").value;
  const nombre = document.getElementById("user-name").value;
  const email = document.getElementById("user-email").value;
  if (id) {
    await fetch(`${API_URL}/users/${id}`, {
      method: "PUT",
      headers: {"Content-Type":"application/json"},
      body: JSON.stringify({ nombre, email })
    });
  } else {
    await fetch(`${API_URL}/users`, {
      method: "POST",
      headers: {"Content-Type":"application/json"},
      body: JSON.stringify({ nombre, email })
    });
  }
  this.reset();
  document.getElementById("user-save-btn").textContent = "Agregar usuario";
  document.getElementById("user-cancel-btn").classList.add("hidden");
  loadUsers();
};
document.getElementById("user-cancel-btn").onclick = function() {
  document.getElementById("user-form").reset();
  document.getElementById("user-save-btn").textContent = "Agregar usuario";
  this.classList.add("hidden");
};

// --------- CRUD Medicamentos ---------
async function loadMeds() {
  const tbody = document.getElementById("meds-table");
  tbody.innerHTML = "";
  try {
    const res = await fetch(`${API_URL}/medicamentos`);
    const meds = await res.json();
    meds.forEach(m => {
      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td>${m.nombre}</td>
        <td>${m.frecuenciaHoras}</td>
        <td>${m.duracionDias}</td>
        <td>
          <button class="action-btn" onclick="editMed('${m.id}','${m.nombre}','${m.frecuenciaHoras}','${m.duracionDias}')">Editar</button>
          <button class="action-btn delete" onclick="deleteMed('${m.id}')">Eliminar</button>
        </td>`;
      tbody.appendChild(tr);
    });
  } catch {}
}
window.editMed = (id, nombre, frecuenciaHoras, duracionDias) => {
  document.getElementById("med-id").value = id;
  document.getElementById("med-name").value = nombre;
  document.getElementById("med-freq").value = frecuenciaHoras;
  document.getElementById("med-duration").value = duracionDias;
  document.getElementById("med-save-btn").textContent = "Actualizar medicamento";
  document.getElementById("med-cancel-btn").classList.remove("hidden");
};
window.deleteMed = async id => {
  if (!confirm("¿Eliminar medicamento?")) return;
  await fetch(`${API_URL}/medicamentos/${id}`, { method: "DELETE" });
  loadMeds();
};
document.getElementById("med-form").onsubmit = async function(e) {
  e.preventDefault();
  const id = document.getElementById("med-id").value;
  const nombre = document.getElementById("med-name").value;
  const frecuenciaHoras = Number(document.getElementById("med-freq").value);
  const duracionDias = Number(document.getElementById("med-duration").value);
  if (id) {
    await fetch(`${API_URL}/medicamentos/${id}`, {
      method: "PUT",
      headers: {"Content-Type":"application/json"},
      body: JSON.stringify({ nombre, frecuenciaHoras, duracionDias })
    });
  } else {
    await fetch(`${API_URL}/medicamentos`, {
      method: "POST",
      headers: {"Content-Type":"application/json"},
      body: JSON.stringify({ nombre, frecuenciaHoras, duracionDias })
    });
  }
  this.reset();
  document.getElementById("med-save-btn").textContent = "Agregar medicamento";
  document.getElementById("med-cancel-btn").classList.add("hidden");
  loadMeds();
};
document.getElementById("med-cancel-btn").onclick = function() {
  document.getElementById("med-form").reset();
  document.getElementById("med-save-btn").textContent = "Agregar medicamento";
  this.classList.add("hidden");
};

// --------- Dosis: Crear inicial, listar, confirmar, historial ---------
async function loadDoseUsers() {
  const sel = document.getElementById("dose-user");
  sel.innerHTML = "";
  const res = await fetch(`${API_URL}/users`);
  const users = await res.json();
  users.forEach(u => {
    const opt = document.createElement("option");
    opt.value = u.id;
    opt.textContent = u.nombre;
    sel.appendChild(opt);
  });
}
async function loadDoseMeds() {
  const sel = document.getElementById("dose-med");
  sel.innerHTML = "";
  const res = await fetch(`${API_URL}/medicamentos`);
  const meds = await res.json();
  meds.forEach(m => {
    const opt = document.createElement("option");
    opt.value = m.id;
    opt.textContent = m.nombre;
    sel.appendChild(opt);
  });
}
document.getElementById("dose-form").onsubmit = async function(e) {
  e.preventDefault();
  const usuarioId = document.getElementById("dose-user").value;
  const medicamentoId = document.getElementById("dose-med").value;
  const fechaHora = document.getElementById("dose-date").value;
  await fetch(`${API_URL}/doses`, {
    method: "POST",
    headers: {"Content-Type":"application/json"},
    body: JSON.stringify({ usuarioId, medicamentoId, fechaHora })
  });
  this.reset();
  loadDoses();
};
async function loadDoses() {
  const tbody = document.getElementById("doses-table");
  tbody.innerHTML = "";
  const res = await fetch(`${API_URL}/doses/pendientes`);
  const doses = await res.json();
  doses.forEach(dose => {
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${dose.usuarioNombre}</td>
      <td>${dose.medicamentoNombre}</td>
      <td>${new Date(dose.fechaHora).toLocaleString()}</td>
      <td>${dose.estado}</td>
      <td>
        ${dose.estado === "pendiente" ? `<button class="action-btn confirm" onclick="confirmDose('${dose.id}')">Confirmar</button>` : ""}
      </td>`;
    tbody.appendChild(tr);
  });
}
window.confirmDose = async id => {
  await fetch(`${API_URL}/doses/confirmar`, {
    method: "POST",
    headers: {"Content-Type":"application/json"},
    body: JSON.stringify({ id })
  });
  loadDoses();
  loadDoseHistory();
};
async function loadDoseHistory() {
  const tbody = document.getElementById("history-table");
  tbody.innerHTML = "";
  const res = await fetch(`${API_URL}/doses/historial`);
  const doses = await res.json();
  doses.forEach(dose => {
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${dose.usuarioNombre}</td>
      <td>${dose.medicamentoNombre}</td>
      <td>${new Date(dose.fechaHora).toLocaleString()}</td>
      <td>${dose.estado}</td>`;
    tbody.appendChild(tr);
  });
}