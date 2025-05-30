const API_URL = "http://localhost:8080/api/patients";

// --------- CRUD Pacientes ---------
async function loadPatients() {
  const tbody = document.getElementById("patients-table");
  tbody.innerHTML = "";
  try {
    const res = await fetch(`${API_URL}`);
    if (!res.ok) throw new Error("No se pudo cargar la lista de pacientes");
    const patients = await res.json();
    patients.forEach(p => {
      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td>${p.name}</td>
        <td>${p.email}</td>
        <td>${p.notificationPermission ? "Sí" : "No"}</td>
        <td>
          <button class="action-btn" onclick="editPatient('${p.patientID}','${p.name}','${p.email}',${p.notificationPermission})">Editar</button>
          <button class="action-btn delete" onclick="deletePatient('${p.patientID}')">Eliminar</button>
        </td>`;
      tbody.appendChild(tr);
    });
  } catch (e) {
    const tr = document.createElement("tr");
    tr.innerHTML = `<td colspan="4" class="error">${e.message}</td>`;
    tbody.appendChild(tr);
  }
}
window.editPatient = (id, name, email, notificationPermission) => {
  document.getElementById("patient-id").value = id;
  document.getElementById("patient-name").value = name;
  document.getElementById("patient-email").value = email;
  document.getElementById("patient-notifications").checked = notificationPermission;
  document.getElementById("patient-save-btn").textContent = "Actualizar paciente";
  document.getElementById("patient-cancel-btn").classList.remove("hidden");
};
window.deletePatient = async id => {
  if (!confirm("¿Eliminar paciente?")) return;
  await fetch(`${API_URL}/${id}`, { method: "DELETE" });
  loadPatients();
};
document.getElementById("patient-form").onsubmit = async function(e) {
  e.preventDefault();
  const id = document.getElementById("patient-id").value;
  const name = document.getElementById("patient-name").value;
  const email = document.getElementById("patient-email").value;
  const notificationPermission = document.getElementById("patient-notifications").checked;
  const dto = { name, email, notificationPermission };
  if (id) {
    // PUT (actualizar)
    await fetch(`${API_URL}/${id}`, {
      method: "PUT",
      headers: {"Content-Type":"application/json"},
      body: JSON.stringify(dto)
    });
  } else {
    // POST (crear)
    await fetch(`${API_URL}`, {
      method: "POST",
      headers: {"Content-Type":"application/json"},
      body: JSON.stringify(dto)
    });
  }
  this.reset();
  document.getElementById("patient-save-btn").textContent = "Agregar paciente";
  document.getElementById("patient-cancel-btn").classList.add("hidden");
  loadPatients();
};
document.getElementById("patient-cancel-btn").onclick = function() {
  document.getElementById("patient-form").reset();
  document.getElementById("patient-save-btn").textContent = "Agregar paciente";
  this.classList.add("hidden");
};

// Inicial
loadPatients();