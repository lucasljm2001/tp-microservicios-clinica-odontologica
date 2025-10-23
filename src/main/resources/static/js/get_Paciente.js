window.deleteBy = function(id){
    fetch('http://localhost:8080/paciente/' + id, { method: 'DELETE' })
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

window.findBy = function(id){
    fetch('http://localhost:8080/paciente/' + id)
        .then(res => {
            if (!res.ok) throw new Error(res.statusText);
            return res.json();
        })
        .then(paciente => {
            console.log('Paciente obtenido:', paciente);
            // si existe el formulario de edición, mostrarlo y llenarlo (mapear campos existentes)
            const updateDiv = document.getElementById('div_paciente_updating');
            if (updateDiv){
                // campos del formulario acorde al JSON: paciente_id, nombre, apellido, numeroContacto, fechaIngreso, domicilio_*, email
                const inputId = document.getElementById('paciente_id');
                const inputNombre = document.getElementById('nombre');
                const inputApellido = document.getElementById('apellido');
                const inputNumero = document.getElementById('numeroContacto');
                const inputFecha = document.getElementById('fechaIngreso');
                const domId = document.getElementById('domicilio_id');
                const domCalle = document.getElementById('domicilio_calle');
                const domNumero = document.getElementById('domicilio_numero');
                const domLocalidad = document.getElementById('domicilio_localidad');
                const domProvincia = document.getElementById('domicilio_provincia');
                const inputEmail = document.getElementById('email');
                if (inputId) inputId.value = paciente.id || '';
                if (inputNombre) inputNombre.value = paciente.nombre || '';
                if (inputApellido) inputApellido.value = paciente.apellido || '';
                if (inputNumero) inputNumero.value = paciente.numeroContacto || '';
                if (inputFecha) inputFecha.value = paciente.fechaIngreso || '';
                if (domId) domId.value = (paciente.domicilio && paciente.domicilio.id) ? paciente.domicilio.id : '';
                if (domCalle) domCalle.value = (paciente.domicilio && paciente.domicilio.calle) ? paciente.domicilio.calle : '';
                if (domNumero) domNumero.value = (paciente.domicilio && paciente.domicilio.numero) ? paciente.domicilio.numero : '';
                if (domLocalidad) domLocalidad.value = (paciente.domicilio && paciente.domicilio.localidad) ? paciente.domicilio.localidad : '';
                if (domProvincia) domProvincia.value = (paciente.domicilio && paciente.domicilio.provincia) ? paciente.domicilio.provincia : '';
                if (inputEmail) inputEmail.value = paciente.email || '';
                updateDiv.style.display = 'block';
                // foco en el primer campo
                if (inputNombre) inputNombre.focus();
            } else {
                alert(JSON.stringify(paciente, null, 2));
            }
        })
        .catch(err => {
            console.error(err);
            alert('Error al obtener paciente');
        });
}

// renderiza la fila de un paciente (alineado con la tabla)
function renderPacienteRowInnerHTML(paciente){
    const updateButton = '<button id="btn_id_' + paciente.id + '" type="button" onclick="findBy('+paciente.id+')" class="btn btn-info btn_id">' + paciente.id + '</button>';
    const deleteButton = '<button id="btn_delete_' + paciente.id + '" type="button" onclick="deleteBy('+paciente.id+')" class="btn btn-danger btn_delete">&times</button>';
    return '<td>' + updateButton + '</td>' +
        '<td class="td_titulo">' + (paciente.nombre? paciente.nombre.toUpperCase(): '') + '</td>' +
        '<td class="td_categoria">' + (paciente.apellido? paciente.apellido.toUpperCase(): '') + '</td>' +
        '<td>' + deleteButton + '</td>';
}

// handler del formulario de actualización
document.addEventListener('DOMContentLoaded', function(){
    const form = document.getElementById('update_paciente_form');
    if (!form) return;
    form.addEventListener('submit', function(evt){
        evt.preventDefault();


        const id = document.getElementById('paciente_id').value || null;
        const nombre = document.getElementById('nombre').value;
        const apellido = document.getElementById('apellido').value;
        const numeroContacto = document.getElementById('numeroContacto').value ? parseInt(document.getElementById('numeroContacto').value) : null;
        const fechaIngreso = document.getElementById('fechaIngreso').value || null;
        const domicilio_id = document.getElementById('domicilio_id').value || null;
        const domicilio = {
            id: domicilio_id ? parseInt(domicilio_id) : null,
            calle: document.getElementById('domicilio_calle').value || null,
            numero: document.getElementById('domicilio_numero').value ? parseInt(document.getElementById('domicilio_numero').value) : null,
            localidad: document.getElementById('domicilio_localidad').value || null,
            provincia: document.getElementById('domicilio_provincia').value || null
        };
        const email = document.getElementById('email').value || null;

        const pacienteToSend = {
            id: id ? parseInt(id) : null,
            nombre: nombre,
            apellido: apellido,
            numeroContacto: numeroContacto,
            fechaIngreso: fechaIngreso,
            domicilio: domicilio,
            email: email
        };

        // elegir método según si existe id (PUT para actualizar, POST para crear)
        const isUpdate = id ? true : false;
        const method = isUpdate ? 'PUT' : 'POST';
        fetch('http://localhost:8080/paciente', {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(pacienteToSend)
        }).then(res => {
            if (!res.ok) throw new Error(res.statusText);
            return res.json();
        }).then(resultPaciente => {
            if (isUpdate) {
                // actualizar fila existente
                const row = document.getElementById('tr_' + resultPaciente.id);
                if (row) row.innerHTML = renderPacienteRowInnerHTML(resultPaciente);
            } else {
                // nueva creación -> añadir fila
                loadAllPatients()
            }
            // ocultar y limpiar formulario
            const updateDiv = document.getElementById('div_paciente_updating');
            if (updateDiv) updateDiv.style.display = 'none';
            // restablecer texto del botón submit
            const submitBtn = form.querySelector('button[type="submit"]');
            if (submitBtn) submitBtn.textContent = 'Modificar';
        }).catch(err => {
            console.error(err);
            alert('Error guardando paciente');
        });
    });
});

// Abrir formulario en modo creación: limpiar campos y cambiar texto del botón
function openCreateForm(){
    const updateDiv = document.getElementById('div_paciente_updating');
    const form = document.getElementById('update_paciente_form');
    if (!updateDiv || !form) return;
    const ids = ['paciente_id','nombre','apellido','numeroContacto','fechaIngreso','domicilio_id','domicilio_calle','domicilio_numero','domicilio_localidad','domicilio_provincia','email'];
    ids.forEach(i => { const el = document.getElementById(i); if (el) el.value = ''; });
    const submitBtn = form.querySelector('button[type="submit"]');
    if (submitBtn) submitBtn.textContent = 'Crear';
    updateDiv.style.display = 'block';
}

function loadAllPatients(){
    //con fetch invocamos a la API de peliculas con el método GET
    //nos devolverá un JSON con una colección de peliculas
    const url = 'http://localhost:8080/paciente';
    const settings = {
        method: 'GET'
    }

    fetch(url,settings)
        .then(response => response.json())
        .then(data => {
            //recorremos la colección de peliculas del JSON
            var table = document.getElementById("pacienteTable");
            table.innerHTML = ''
            for(paciente of data){
                //por cada pelicula armaremos una fila de la tabla
                //cada fila tendrá un id que luego nos permitirá borrar la fila si eliminamos la pelicula
                var table = document.getElementById("pacienteTable");

                var pacienteRow =table.insertRow();
                let tr_id = 'tr_' + paciente.id;
                pacienteRow.id = tr_id;


                //por cada pelicula creamos un boton delete que agregaremos en cada fila para poder eliminar la misma
                //dicho boton invocara a la funcion de java script deleteByKey que se encargará
                //de llamar a la API para eliminar una pelicula
                let deleteButton = '<button' +
                    ' id=' + '\"' + 'btn_delete_' + paciente.id + '\"' +
                    ' type="button" onclick="deleteBy('+paciente.id+')" class="btn btn-danger btn_delete">' +
                    '&times' +
                    '</button>';

                //por cada pelicula creamos un boton que muestra el id y que al hacerle clic invocará
                //a la función de java script findBy que se encargará de buscar la pelicula que queremos
                //modificar y mostrar los datos de la misma en un formulario.
                let updateButton = '<button' +
                    ' id=' + '\"' + 'btn_id_' + paciente.id + '\"' +
                    ' type="button" onclick="findBy('+paciente.id+')" class="btn btn-info btn_id">' +
                    paciente.id +
                    '</button>';

                //armamos cada columna de la fila
                //como primer columna pondremos el boton modificar
                //luego los datos de la pelicula
                //como ultima columna el boton eliminar
                pacienteRow.innerHTML = '<td>' + updateButton + '</td>' +
                    '<td class=\"td_titulo\">' + paciente.nombre.toUpperCase() + '</td>' +
                    '<td class=\"td_categoria\">' + paciente.apellido.toUpperCase() + '</td>' +
                    '<td>' + deleteButton + '</td>';

            };


        })
}


function searchByName(name) {
    const url = 'http://localhost:8080/paciente/buscar?nombre=' + encodeURIComponent(name);
    const settings = { method: 'GET' };

    fetch(url, settings)
        .then(response => response.json())
        .then(paciente => {
            console.log(paciente)
            // limpiar tabla
            const table = document.getElementById("pacienteTable");
            table.innerHTML = ''
            if (!table) return;
            // eliminar todas las filas excepto la cabecera
            while (table.rows.length > 1) {
                table.deleteRow(1);
            }
            // agregar filas encontradas
            const pacienteRow = table.insertRow();
            let tr_id = 'tr_' + paciente.id;
            pacienteRow.id = tr_id;
            pacienteRow.innerHTML = renderPacienteRowInnerHTML(paciente);

        })
        .catch(err => {
            console.error(err);
        });
}

window.addEventListener('load', function(){
    loadAllPatients();
    const btnSearch = document.getElementById('btn_search');
    const btnClear = document.getElementById('btn_search_clear');
    const inputSearch = document.getElementById('search_nombre');
    const btnCreate = document.getElementById('btn_add');
    if (btnSearch && inputSearch) btnSearch.addEventListener('click', () => searchByName(inputSearch.value));
    if (btnClear && inputSearch) btnClear.addEventListener('click', () => { inputSearch.value = ''; loadAllPatients(); });
    if (btnCreate) btnCreate.addEventListener('click', () => openCreateForm());
});
