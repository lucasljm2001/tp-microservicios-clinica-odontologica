// filepath: /Users/naranjax/IdeaProjects/tp-microservicios-clinica-odontologica/src/main/resources/static/js/get_Turno.js

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

window.findTurnoBy = function(id){
    fetch('http://localhost:8080/turnos/' + id)
        .then(res => { if (!res.ok) throw new Error(res.statusText); return res.json(); })
        .then(turnoDTO => {
            // turnoDTO: {id, fecha, pacienteId, odontologoId}
            const updateDiv = document.getElementById('div_turno_updating');
            if (!updateDiv) { alert(JSON.stringify(turnoDTO, null, 2)); return; }
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

            // intentar obtener nombres para mostrar (opcionales)
            if (turnoDTO.pacienteId) {
                fetch('http://localhost:8080/paciente/' + turnoDTO.pacienteId)
                    .then(r => r.ok ? r.json() : null)
                    .then(p => { if (inputPacienteNombre) inputPacienteNombre.value = p ? (p.nombre + ' ' + p.apellido) : ''; })
                    .catch(()=>{});
            }
            if (turnoDTO.odontologoId) {
                fetch('http://localhost:8080/odontologo/' + turnoDTO.odontologoId)
                    .then(r => r.ok ? r.json() : null)
                    .then(o => { if (inputOdontologoNombre) inputOdontologoNombre.value = o ? (o.nombre + ' ' + o.apellido) : ''; })
                    .catch(()=>{});
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

// helper para obtener nombre completo de paciente/odontologo por id
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

// handler del formulario de actualización/creación
document.addEventListener('DOMContentLoaded', function(){
    const form = document.getElementById('update_turno_form');
    if (!form) return;
    form.addEventListener('submit', function(evt){
        evt.preventDefault();
        const id = document.getElementById('turno_id').value || null;
        const fecha = document.getElementById('fechaHora').value || null;
        const pacienteIdVal = document.getElementById('paciente_id').value || null;
        const odontologoIdVal = document.getElementById('odontologo_id').value || null;

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
        }).then(result => {
            // resultado puede ser TurnoDTO (POST) o Turno (PUT)
            // recargar lista para simplificar
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
    const ids = ['turno_id','fechaHora','paciente_id','paciente_nombre','odontologo_id','odontologo_nombre'];
    ids.forEach(i => { const el = document.getElementById(i); if (el) el.value = ''; });
    const submitBtn = form.querySelector('button[type="submit"]');
    if (submitBtn) submitBtn.textContent = 'Crear';
    updateDiv.style.display = 'block';
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

