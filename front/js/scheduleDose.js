const API_URL = "http://localhost:8080/api/scheduledoses";

// Cargar pacientes (para select)
async function loadPatients() {
  const res = await fetch("http://localhost:8080/api/patients");
  const patients = await res.json();
  const select = document.getElementById("dose-patient");
  select.innerHTML = "";
  patients.forEach(p => {
    const opt = document.createElement("option");
    opt.value = p.patientID;
    opt.textContent = p.name + " (" + p.email + ")";
    select.appendChild(opt);
  });
}

// Cargar medicamentos (para select)
async function loadMedications() {
  const res = await fetch("http://localhost:8080/api/medications");
  const meds = await res.json();
  const select = document.getElementById("dose-medication");
  select.innerHTML = "";
  meds.forEach(m => {
    const opt = document.createElement("option");
    opt.value = m.medicationID;
    opt.textContent = m.name + " (" + m.dosage + ")";
    select.appendChild(opt);
  });
}

// Estado a texto
function statusText(status) {
  switch (status) {
    case 0: return "Pendiente";
    case 1: return "Confirmada";
    case 2: return "No tomada";
    default: return "Desconocido";
  }
}

// Cargar dosis programadas
async function loadScheduleDoses() {
  const tbody = document.getElementById("scheduledoses-table");
  tbody.innerHTML = "";
  try {
    const res = await fetch(API_URL);
    if (!res.ok) throw new Error("No se pudo cargar la lista de dosis programadas");
    const doses = await res.json();
    doses.forEach(d => {
      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td>${d.patient?.name || ""}</td>
        <td>${d.medication?.name || ""}</td>
        <td>${d.startDate ? d.startDate.replace("T", " ").substring(0, 16) : ""}</td>
        <td>${d.durationDays}</td>
        <td>${statusText(d.confirmationStatus)}</td>
        <td>
          <button class="action-btn" onclick="editScheduleDose('${d.doseID}','${d.patient?.patientID}','${d.medication?.medicationID}','${d.durationDays}','${d.confirmationStatus}')">Editar</button>
          <button class="action-btn delete" onclick="deleteScheduleDose('${d.doseID}')">Eliminar</button>
        </td>`;
      tbody.appendChild(tr);
    });
  } catch (e) {
    const tr = document.createElement("tr");
    tr.innerHTML = `<td colspan="6" class="error">${e.message}</td>`;
    tbody.appendChild(tr);
  }
}

// Editar dosis (ya no se edita la fecha)
window.editScheduleDose = (id, patientID, medicationID, durationDays, confirmationStatus) => {
  document.getElementById("dose-id").value = id;
  document.getElementById("dose-patient").value = patientID;
  document.getElementById("dose-medication").value = medicationID;
  document.getElementById("dose-duration").value = durationDays;
  document.getElementById("dose-status").value = confirmationStatus;
  document.getElementById("dose-save-btn").textContent = "Actualizar dosis";
  document.getElementById("dose-cancel-btn").classList.remove("hidden");
};

// Eliminar dosis
window.deleteScheduleDose = async id => {
  if (!confirm("¿Eliminar dosis programada?")) return;
  const res = await fetch(`${API_URL}/${id}`, { method: "DELETE" });
  if (!res.ok) {
    const msg = await res.json().catch(() => ({}));
    alert(msg.message || "Error al eliminar");
  }
  loadScheduleDoses();
};

// Guardar/actualizar dosis
document.getElementById("scheduledose-form").onsubmit = async function(e) {
  e.preventDefault();
  const id = document.getElementById("dose-id").value;
  const patientID = parseInt(document.getElementById("dose-patient").value, 10);
  const medicationID = parseInt(document.getElementById("dose-medication").value, 10);
  const durationDays = parseInt(document.getElementById("dose-duration").value, 10);
  const confirmationStatus = parseInt(document.getElementById("dose-status").value, 10);

  // Generar la fecha/hora actual en formato ISO
  const now = new Date();
  const localISO = now.toISOString().slice(0, 16); // "YYYY-MM-DDTHH:MM"
  // Tu backend espera LocalDateTime, que acepta formato "YYYY-MM-DDTHH:MM"
  const startDate = localISO;

  const dto = { patientID, medicationID, startDate, durationDays, confirmationStatus };

  if (id) {
    await fetch(`${API_URL}/${id}`, {
      method: "PUT",
      headers: {"Content-Type":"application/json"},
      body: JSON.stringify(dto)
    });
  } else {
    await fetch(API_URL, {
      method: "POST",
      headers: {"Content-Type":"application/json"},
      body: JSON.stringify(dto)
    });
  }
  this.reset();
  document.getElementById("dose-save-btn").textContent = "Agregar dosis";
  document.getElementById("dose-cancel-btn").classList.add("hidden");
  loadScheduleDoses();
};

document.getElementById("dose-cancel-btn").onclick = function() {
  document.getElementById("scheduledose-form").reset();
  document.getElementById("dose-save-btn").textContent = "Agregar dosis";
  this.classList.add("hidden");
};

// Inicial
async function init() {
  await loadPatients();
  await loadMedications();
  await loadScheduleDoses();
}
init();