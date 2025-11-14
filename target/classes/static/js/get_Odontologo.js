window.deleteBy = function(id){
    fetch('http://localhost:8080/odontologo/' + id, { method: 'DELETE' })
        .then(response => {
            if (response.ok) {
                const row = document.getElementById('tr_' + id);
                if (row) row.remove();
            } else {
                response.text().then(t => alert('Error al eliminar: ' + t));
            }
        })
        .catch(err => {
            console.error(err);
        });
}
window.findBy = function(id) {
    fetch('http://localhost:8080/odontologo/' + id)
        .then(res => {
            if (!res.ok) throw new Error(res.statusText);
            return res.json();
        })
        .then(odontologo => {
            console.log('Odontologo obtenido:', odontologo);
            const updateDiv = document.getElementById('div_odontologo_updating');
            if (updateDiv) {
                const inputId = document.getElementById('odontologo_id');
                const inputNombre = document.getElementById('nombre');
                const inputApellido = document.getElementById('apellido');
                const inputMatricula = document.getElementById('matricula');
                if (inputId) inputId.value = odontologo.id || '';
                if (inputNombre) inputNombre.value = odontologo.nombre || '';
                if (inputApellido) inputApellido.value = odontologo.apellido || '';
                if (inputMatricula) inputMatricula.value = odontologo.matricula || '';

                updateDiv.style.display = 'block';
                if (inputNombre) inputNombre.focus();
            } else {
                alert(JSON.stringify(odontologo, null, 2));
            }
        })
        .catch(err => {
            console.error(err);
            alert('Error al obtener odontologo: ' + err.message);
        })
}
function renderOdontologoRowInnerHTML(odontologo) {
    const updateButton = '<button id="btn_id_' + odontologo.id + '" type="button" onclick="findBy(' + odontologo.id + ')" class="btn btn-info btn_id">' + odontologo.id + '</button>';
    const deleteButton = '<button id="btn_delete_' + odontologo.id + '" type="button" onclick="deleteBy(' + odontologo.id + ')" class="btn btn-danger btn_delete">&times;</button>';

    return (
        '<td>' + updateButton + '</td>' +
        '<td class="td_titulo">' + (odontologo.nombre ? odontologo.nombre.toUpperCase() : '') + '</td>' +
        '<td class="td_categoria">' + (odontologo.apellido ? odontologo.apellido.toUpperCase() : '') + '</td>' +
        '<td class="td_matricula">' + (odontologo.matricula ? odontologo.matricula.toUpperCase() : '') + '</td>' + // ✅ NUEVA COLUMNA
        '<td>' + deleteButton + '</td>'
    );
}

document.addEventListener('DOMContentLoaded', function(){
    const form = document.getElementById('update_odontologo_form');
    if (!form) return;
    form.addEventListener('submit', function(evt){
        evt.preventDefault();


        const id = document.getElementById('odontologo_id').value || null;
        const nombre = document.getElementById('nombre').value;
        const apellido = document.getElementById('apellido').value;
        const matricula = document.getElementById('matricula').value;
        const odontologoToSend = {
            id: id ? parseInt(id) : null,
            nombre: nombre,
            apellido: apellido,
            matricula: matricula
        };

        const isUpdate = id ? true : false;
        const method = isUpdate ? 'PUT' : 'POST';
        fetch('http://localhost:8080/odontologo', {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(odontologoToSend)
        }).then(res => {
            if (!res.ok) throw new Error(res.statusText);
            return res.json();
        }).then(resultOdontologo => {
            if (isUpdate) {
                const row = document.getElementById('tr_' + resultOdontologo.id);
                if (row) row.innerHTML = renderOdontologoRowInnerHTML(resultOdontologo);
            } else {
                loadAllOdontologos()
            }
            const updateDiv = document.getElementById('div_odontologo_updating');
            if (updateDiv) updateDiv.style.display = 'none';
            const submitBtn = form.querySelector('button[type="submit"]');
            if (submitBtn) submitBtn.textContent = 'Modificar';
        }).catch(err => {
            console.error(err);
            alert('Error guardando odontologo: ' + err.message);
        });
    });
});
function openCreateForm() {
    const updateDiv = document.getElementById('div_odontologo_updating');
    const form = document.getElementById('update_odontologo_form');
    if (!updateDiv || !form) return;

    const ids = ['odontologo_id', 'nombre', 'apellido', 'matricula']; // ✅ nombres correctos
    ids.forEach(i => {
        const el = document.getElementById(i);
        if (el) el.value = '';
    });

    const submitBtn = form.querySelector('button[type="submit"]');
    if (submitBtn) submitBtn.textContent = 'Crear';
    updateDiv.style.display = 'block';
}
function loadAllOdontologos(){

    const url = 'http://localhost:8080/odontologo';
    const settings = {
        method: 'GET'
    }
    fetch(url,settings)
        .then(response => response.json())
        .then(data => {
            var table = document.getElementById("odontologoTable");
            table.innerHTML = ''
            for(odontologo of data){
                var table = document.getElementById("odontologoTable");

                var odontologoRow =table.insertRow();
                let tr_id = 'tr_' + odontologo.id;
                odontologoRow.id = tr_id;


                let deleteButton = '<button' +
                    ' id=' + '\"' + 'btn_delete_' + odontologo.id + '\"' +
                    ' type="button" onclick="deleteBy('+odontologo.id+')" class="btn btn-danger btn_delete">' +
                    '&times' +
                    '</button>';

                let updateButton = '<button' +
                    ' id=' + '\"' + 'btn_id_' + odontologo.id + '\"' +
                    ' type="button" onclick="findBy('+odontologo.id+')" class="btn btn-info btn_id">' +
                    odontologo.id +
                    '</button>';

                odontologoRow.innerHTML = '<td>' + updateButton + '</td>' +
                    '<td class="td_titulo">' + (odontologo.nombre ? odontologo.nombre.toUpperCase() : '') + '</td>' +
                    '<td class="td_categoria">' + (odontologo.apellido ? odontologo.apellido.toUpperCase() : '') + '</td>' +
                    '<td class="td_matricula">' + (odontologo.matricula ? odontologo.matricula.toUpperCase() : '') + '</td>' +
                    '<td>' + deleteButton + '</td>';
            };


        })
}
function searchByName(name) {
    const url = 'http://localhost:8080/odontologo/buscar?matricula=' + name;
    const settings = { method: 'GET' };
    console.log(url)

    fetch(url, settings)
        .then(response => response.json())
        .then(odontologo => {
            console.log(odontologo)
            const table = document.getElementById("odontologoTable");
            table.innerHTML = ''
            if (!table) return;
            while (table.rows.length > 1) {
                table.deleteRow(1);
            }
            const odontologoRow = table.insertRow();
            let tr_id = 'tr_' + odontologo.id;
            odontologoRow.id = tr_id;
            odontologoRow.innerHTML = renderOdontologoRowInnerHTML(odontologo);

        })
        .catch(err => {
            console.error(err);
        });
}
window.addEventListener('load', function(){
    loadAllOdontologos();
    const btnSearch = document.getElementById('btn_search');
    const btnClear = document.getElementById('btn_search_clear');
    const inputSearch = document.getElementById('search_nombre');
    const btnCreate = document.getElementById('btn_add');
    if (btnSearch && inputSearch) btnSearch.addEventListener('click', () => searchByName(inputSearch.value));
    if (btnClear && inputSearch) btnClear.addEventListener('click', () => { inputSearch.value = ''; loadAllOdontologos(); });
    if (btnCreate) btnCreate.addEventListener('click', () => openCreateForm());
});
