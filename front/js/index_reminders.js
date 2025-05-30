// Cargar todos los pacientes al inicio (para sugerencia)
let allPatients = [];
let selectedPatient = null;

// Cargar pacientes
async function loadPatients() {
  const res = await fetch("http://localhost:8080/api/patients");
  allPatients = await res.json();
}
loadPatients();

document.getElementById('search-patient').addEventListener('input', function() {
  const val = this.value.trim().toLowerCase();
  const ul = document.getElementById('patient-list');
  ul.innerHTML = "";
  if (val.length === 0) return;
  const matches = allPatients.filter(p => p.name.toLowerCase().includes(val));
  matches.forEach(p => {
    const li = document.createElement('li');
    li.textContent = `${p.name} (${p.patientID})`;
    li.style.cursor = "pointer";
    li.onclick = () => selectPatient(p);
    ul.appendChild(li);
  });
});

async function selectPatient(patient) {
  selectedPatient = patient;
  document.getElementById('search-patient').value = patient.name;
  document.getElementById('patient-list').innerHTML = "";
  document.getElementById('historial-title').style.display = "block";
  await loadReminders(patient.patientID);
}

async function loadReminders(patientID) {
  const table = document.getElementById("reminders-table");
  const tbody = table.querySelector("tbody");
  tbody.innerHTML = "";
  table.style.display = "table";
  let res = await fetch(`http://localhost:8080/api/reminders/byPatient/${patientID}`);
  if (!res.ok) {
    tbody.innerHTML = `<tr><td colspan="2">No se pudo cargar el historial.</td></tr>`;
    return;
  }
  const reminders = await res.json();
  if (reminders.length === 0) {
    tbody.innerHTML = `<tr><td colspan="2">No hay recordatorios para este paciente.</td></tr>`;
    return;
  }
  reminders.forEach(r => {
    const tr = document.createElement("tr");
    // r.sendAt debería estar en formato ISO, ej: '2025-05-30T15:45:12'
    const fecha = r.sendAt ? r.sendAt.replace("T", " ").substring(0, 16) : "";
    tr.innerHTML = `
      <td>${fecha}</td>
      <td>${r.status ? "Tomado" : "No tomado"}</td>
    `;
    tbody.appendChild(tr);
  });
}