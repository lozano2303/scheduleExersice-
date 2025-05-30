const API_URL = "http://localhost:8080/api/medications";

// --------- CRUD Medicamentos ---------
async function loadMedications() {
  const tbody = document.getElementById("medications-table");
  tbody.innerHTML = "";
  try {
    const res = await fetch(`${API_URL}`);
    if (!res.ok) throw new Error("No se pudo cargar la lista de medicamentos");
    const medications = await res.json();
    medications.forEach(m => {
      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td>${m.name}</td>
        <td>${m.dosage}</td>
        <td>${m.frequencyHours}</td>
        <td>
          <button class="action-btn" onclick="editMedication('${m.medicationID}','${m.name}','${m.dosage}',${m.frequencyHours})">Editar</button>
          <button class="action-btn delete" onclick="deleteMedication('${m.medicationID}')">Eliminar</button>
        </td>`;
      tbody.appendChild(tr);
    });
  } catch (e) {
    const tr = document.createElement("tr");
    tr.innerHTML = `<td colspan="4" class="error">${e.message}</td>`;
    tbody.appendChild(tr);
  }
}

window.editMedication = (id, name, dosage, frequencyHours) => {
  document.getElementById("medication-id").value = id;
  document.getElementById("medication-name").value = name;
  document.getElementById("medication-dosage").value = dosage;
  document.getElementById("medication-frequency").value = frequencyHours;
  document.getElementById("medication-save-btn").textContent = "Actualizar medicamento";
  document.getElementById("medication-cancel-btn").classList.remove("hidden");
};

window.deleteMedication = async id => {
  if (!confirm("¿Eliminar medicamento?")) return;
  const res = await fetch(`${API_URL}/${id}`, { method: "DELETE" });
  if (!res.ok) {
    const msg = await res.json().catch(() => ({}));
    alert(msg.message || "Error al eliminar");
  }
  loadMedications();
};

document.getElementById("medication-form").onsubmit = async function(e) {
  e.preventDefault();
  const id = document.getElementById("medication-id").value;
  const name = document.getElementById("medication-name").value;
  const dosage = document.getElementById("medication-dosage").value;
  const frequencyHours = parseInt(document.getElementById("medication-frequency").value, 10);
  const dto = { name, dosage, frequencyHours };
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
  document.getElementById("medication-save-btn").textContent = "Agregar medicamento";
  document.getElementById("medication-cancel-btn").classList.add("hidden");
  loadMedications();
};

document.getElementById("medication-cancel-btn").onclick = function() {
  document.getElementById("medication-form").reset();
  document.getElementById("medication-save-btn").textContent = "Agregar medicamento";
  this.classList.add("hidden");
};

// Inicial
loadMedications();