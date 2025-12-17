window.deleteTurnoBy = function(id){
    fetch('http://localhost:8080/turnos/' + id, { method: 'DELETE' })
        .then(response => {
            if (response.ok) {
                const row = document.getElementById('tr_' + id);
                if (row) row.remove();
            } else {
                response.text().then(t => alert('Error al eliminar: ' + t));
            }
        })
        .catch(err => console.error(err));
}


function setSelectInitialValue(sel, id, label) {
    if (!sel) return;
    if (!id) { sel.value = ''; return; }
    let opt = sel.querySelector('option[value="' + id + '"]');
    if (!opt) {
        opt = document.createElement('option');
        opt.value = id;
        opt.textContent = label || ('Id: ' + id);
        sel.appendChild(opt);
    } else if (label) {
        opt.textContent = label;
    }
    opt.selected = true;
    sel.value = id;
}

window.findTurnoBy = async function(id){
    const selPaciente = document.getElementById('paciente_nombre');
    const selOdontologo = document.getElementById('odontologo_nombre');
    fetch('http://localhost:8080/turnos/' + id)
        .then(res => { if (!res.ok) throw new Error(res.statusText); return res.json(); })
        .then(async turnoDTO => {
            const updateDiv = document.getElementById('div_turno_updating');
            if (!updateDiv) {
                alert(JSON.stringify(turnoDTO, null, 2));
                return;
            }
            const inputId = document.getElementById('turno_id');
            const inputFecha = document.getElementById('fechaHora');
            const inputPacienteId = document.getElementById('paciente_id');
            const inputPacienteNombre = document.getElementById('paciente_nombre');
            const inputOdontologoId = document.getElementById('odontologo_id');
            const inputOdontologoNombre = document.getElementById('odontologo_nombre');

            if (inputId) inputId.value = turnoDTO.id || '';
            if (inputFecha) inputFecha.value = turnoDTO.fecha || '';
            if (inputPacienteId) inputPacienteId.value = turnoDTO.pacienteId || '';
            if (inputOdontologoId) inputOdontologoId.value = turnoDTO.odontologoId || '';

            console.log("El turno es:", turnoDTO)
            console.log("El paciente es:", inputPacienteNombre)
            if (inputPacienteNombre) {
                const pid = turnoDTO.pacienteId || '';
                if (inputPacienteNombre.tagName === 'SELECT') {
                    const nombrePaciente = await fetchNombrePaciente(pid)
                    await loadPatientsIntoSelect()
                    setSelectInitialValue(selPaciente, pid, nombrePaciente + ' (id:' + pid + ')')
                } else {
                    inputPacienteNombre.value = pid ? String(pid) : '';
                }
            }

            if (inputOdontologoNombre) {
                const oid = turnoDTO.odontologoId || '';
                if (inputOdontologoNombre.tagName === 'SELECT') {
                    const nombreOdontologo = await fetchNombreOdontologo(oid)
                    await loadOdontologosIntoSelect()
                    setSelectInitialValue(selOdontologo, oid, nombreOdontologo + ' (id:' + oid + ')')
                } else {
                    inputOdontologoNombre.value = oid ? String(oid) : '';
                }
            }


            updateDiv.style.display = 'block';
            if (inputFecha) inputFecha.focus();
        })
        .catch(err => { console.error(err); alert('Error al obtener turno'); });
}

function renderTurnoRowInnerHTML(turno, pacienteNombre, odontologoNombre){
    const updateButton = '<button id="btn_id_' + turno.id + '" type="button" onclick="findTurnoBy('+turno.id+')" class="btn btn-info btn_id">' + turno.id + '</button>';
    const deleteButton = '<button id="btn_delete_' + turno.id + '" type="button" onclick="deleteTurnoBy('+turno.id+');" class="btn btn-danger btn_delete">&times</button>';
    return '<td>' + updateButton + '</td>' +
        '<td class="td_titulo">' + (turno.fecha? turno.fecha : '') + '</td>' +
        '<td class="td_categoria">' + (pacienteNombre? pacienteNombre : ('Id: ' + (turno.pacienteId || ''))) + '</td>' +
        '<td class="td_categoria">' + (odontologoNombre? odontologoNombre : ('Id: ' + (turno.odontologoId || ''))) + '</td>' +
        '<td>' + deleteButton + '</td>';
}

function fetchNombrePaciente(id){
    if (!id) return Promise.resolve('');
    return fetch('http://localhost:8080/paciente/' + id)
        .then(r => r.ok ? r.json() : null)
        .then(p => p ? (p.nombre + ' ' + p.apellido) : '')
        .catch(()=> '');
}
function fetchNombreOdontologo(id){
    if (!id) return Promise.resolve('');
    return fetch('http://localhost:8080/odontologo/' + id)
        .then(r => r.ok ? r.json() : null)
        .then(o => o ? (o.nombre + ' ' + o.apellido) : '')
        .catch(()=> '');
}

// Carga la lista de pacientes en el select #paciente_nombre.
function loadPatientsIntoSelect(){
    const sel = document.getElementById('paciente_nombre');
    if (!sel) return Promise.resolve();
    return fetch('http://localhost:8080/paciente')
        .then(r => { if (!r.ok) throw new Error('Error cargando pacientes'); return r.json(); })
        .then(data => {
            sel.innerHTML = '<option value="">-- Seleccione un paciente --</option>';
            data.forEach(p => {
                const opt = document.createElement('option');
                opt.value = p.id;
                opt.text = (p.nombre ? p.nombre : '') + ' ' + (p.apellido ? p.apellido : '') + ' (id:' + p.id + ')';
                sel.appendChild(opt);
            });
        })
        .catch(err => {
            console.error('No se pudieron cargar pacientes:', err);
        });
}


function loadOdontologosIntoSelect(){
    const sel = document.getElementById('odontologo_nombre');
    if (!sel) return Promise.resolve();
    return fetch('http://localhost:8080/odontologo')
        .then(r => { if (!r.ok) throw new Error('Error cargando odontologos'); return r.json(); })
        .then(data => {
            sel.innerHTML = '<option value="">-- Seleccione un odontologo --</option>';
            data.forEach(p => {
                const opt = document.createElement('option');
                opt.value = p.id;
                opt.text = (p.nombre ? p.nombre : '') + ' ' + (p.apellido ? p.apellido : '') + ' (id:' + p.id + ')';
                sel.appendChild(opt);
            });
        })
        .catch(err => {
            console.error('No se pudieron cargar pacientes:', err);
        });
}

document.addEventListener('DOMContentLoaded', function(){
    const form = document.getElementById('update_turno_form');
    if (!form) return;
    form.addEventListener('submit', function(evt){
        evt.preventDefault();
        const id = document.getElementById('turno_id').value || null;
        const fecha = document.getElementById('fechaHora').value || null;
        const pacienteIdVal = document.getElementById('paciente_nombre').value || null;
        const odontologoIdVal = document.getElementById('odontologo_nombre').value || null;

        const isUpdate = !!id;
        var turnoToSend;
        if (isUpdate) {
            turnoToSend = {
                id: id,
                fecha: fecha,
                pacienteId: pacienteIdVal,
                odontologoId: odontologoIdVal
            };
        }else{
            turnoToSend = {
                fecha: fecha,
                pacienteId: pacienteIdVal,
                odontologoId: odontologoIdVal
            };
        }


        const method = isUpdate ? 'PUT' : 'POST';
        const url = 'http://localhost:8080/turnos';

        fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(turnoToSend)
        }).then(res => {
            if (!res.ok) throw new Error(res.statusText);
            return res.json();
        }).then(() => {
            loadAllTurnos();
            const updateDiv = document.getElementById('div_turno_updating');
            if (updateDiv) updateDiv.style.display = 'none';
        }).catch(err => { console.error(err); alert('Error guardando turno'); });
    });


});

function openCreateTurnoForm(){
    const updateDiv = document.getElementById('div_turno_updating');
    const form = document.getElementById('update_turno_form');
    if (!updateDiv || !form) return;
    const ids = ['turno_id','fechaHora','paciente_nombre','odontologo_nombre'];
    ids.forEach(i => { const el = document.getElementById(i); if (el) el.value = ''; });
    const submitBtn = form.querySelector('button[type="submit"]');
    if (submitBtn) submitBtn.textContent = 'Crear';
    // refrescar lista de pacientes antes de mostrar
    loadPatientsIntoSelect().then(()=>{
        updateDiv.style.display = 'block';
    });
    loadOdontologosIntoSelect().then(()=>{
        updateDiv.style.display = 'block';
    });
}

function loadAllTurnos(){
    const url = 'http://localhost:8080/turnos';
    fetch(url)
        .then(res => res.json())
        .then(async data => {
            const table = document.getElementById('turnoTable');
            table.innerHTML = '';
            for (let turno of data){
                // obtener nombres en paralelo
                const [pacienteNombre, odontologoNombre] = await Promise.all([
                    fetchNombrePaciente(turno.pacienteId),
                    fetchNombreOdontologo(turno.odontologoId)
                ]);
                const row = document.getElementById('turnoTable').insertRow();
                row.id = 'tr_' + turno.id;
                row.innerHTML = renderTurnoRowInnerHTML(turno, pacienteNombre, odontologoNombre);
            }
        })
        .catch(err => { console.error(err); });
}

function searchByPacienteName(name){
    // buscar localmente: cargar todos y filtrar por nombre del paciente
    fetch('http://localhost:8080/turnos')
        .then(res => res.json())
        .then(async data => {
            const table = document.getElementById('turnoTable');
            table.innerHTML = '';
            for (let turno of data){
                const pacienteNombre = await fetchNombrePaciente(turno.pacienteId);
                if (pacienteNombre.toLowerCase().includes(name.toLowerCase())){
                    const odontologoNombre = await fetchNombreOdontologo(turno.odontologoId);
                    const row = document.getElementById('turnoTable').insertRow();
                    row.id = 'tr_' + turno.id;
                    row.innerHTML = renderTurnoRowInnerHTML(turno, pacienteNombre, odontologoNombre);
                }
            }
        })
        .catch(err => console.error(err));
}

window.addEventListener('load', function(){
    loadAllTurnos();
    const btnSearch = document.getElementById('btn_search');
    const btnClear = document.getElementById('btn_search_clear');
    const inputSearch = document.getElementById('search_nombre');
    const btnCreate = document.getElementById('btn_add');
    if (btnSearch && inputSearch) btnSearch.addEventListener('click', () => searchByPacienteName(inputSearch.value));
    if (btnClear && inputSearch) btnClear.addEventListener('click', () => { inputSearch.value = ''; loadAllTurnos(); });
    if (btnCreate) btnCreate.addEventListener('click', () => openCreateTurnoForm());
});
