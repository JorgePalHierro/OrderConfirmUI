 /* ==============================
       Pestaña Filtro
       ============================== */
    // Filtrado dinámico por número de Orden (mientras se escribe)
    document.getElementById('orden').addEventListener('keyup', function () {
      const searchTerm = this.value.toLowerCase();
      const rows = document.querySelectorAll('table tbody tr');
      rows.forEach(row => {
        const ordenCell = row.cells[4];
        const ordenText = ordenCell.textContent || ordenCell.innerText;
        row.style.display = ordenText.includes(searchTerm) ? '' : 'none';
      });
    });

    // Filtro por número de Orden vía botón en Filtro
    document.getElementById('filter-order-button').addEventListener('click', function () {
      const form = document.getElementById('filter-form');
      const formData = new FormData(form);
      const spinner = document.getElementById('loading-spinner');
      spinner.style.display = 'inline-block';
      fetch('/filtrarOrden', {
        method: 'POST',
        body: formData,
      })
      .then(response => response.text())
      .then(html => {
        spinner.style.display = 'none';
        const parser = new DOMParser();
        const doc = parser.parseFromString(html, 'text/html');
        const newTableBody = doc.querySelector('table tbody');
        document.querySelector('table tbody').innerHTML = newTableBody.innerHTML;
      })
      .catch(error => {
        spinner.style.display = 'none';
        console.error('Error al filtrar:', error);
      });
    });

    // Filtro por tienda, fecha y estado en Filtro
    document.getElementById('filter-button').addEventListener('click', function () {
      const form = document.getElementById('filter-form');
      const formData = new FormData(form);
      const spinner = document.getElementById('loading-spinner');
      spinner.style.display = 'inline-block';
      fetch('/filtrar', {
        method: 'POST',
        body: formData,
      })
      .then(response => response.text())
      .then(html => {
        spinner.style.display = 'none';
        const parser = new DOMParser();
        const doc = parser.parseFromString(html, 'text/html');
        const newTableBody = doc.querySelector('table tbody');
        document.querySelector('table tbody').innerHTML = newTableBody.innerHTML;
      })
      .catch(error => {
        spinner.style.display = 'none';
        console.error('Error al filtrar:', error);
      });
    });

    // Mostrar u ocultar filtros según la opción seleccionada en Filtro
    document.getElementById('filter-type').addEventListener('change', function () {
      const selectedValue = this.value;
      if (selectedValue === 'orden') {
        document.getElementById('orden-filter').style.display = 'block';
        document.getElementById('store-filter').style.display = 'none';
        document.getElementById('status-filter').style.display = 'none';
        document.getElementById('date-filter').style.display = 'none';
        document.getElementById('filter-button').style.display = 'none';
      } else if (selectedValue === 'tienda-fecha-estado') {
        document.getElementById('orden-filter').style.display = 'none';
        document.getElementById('store-filter').style.display = 'block';
        document.getElementById('status-filter').style.display = 'block';
        document.getElementById('date-filter').style.display = 'block';
        document.getElementById('filter-button').style.display = 'block';
      }
    });

    /* ==============================
       Pestaña Confirmar
       ============================== */
    // Mostrar u ocultar filtros según la opción seleccionada en Confirmar
    document.getElementById('filter-type-confirm').addEventListener('change', function () {
      const selectedValue = this.value;
      if (selectedValue === 'orden') {
        document.getElementById('orden-filter-confirm').style.display = 'block';
        document.getElementById('tienda-terminal-transaccion-filter').style.display = 'none';
      } else if (selectedValue === 'tienda-terminal-transaccion') {
        document.getElementById('orden-filter-confirm').style.display = 'none';
        document.getElementById('tienda-terminal-transaccion-filter').style.display = 'block';
      }
    });

    // Filtro por número de Orden en Confirmar vía botón
    document.getElementById('filter-order-button-confirm').addEventListener('click', function () {
      const form = document.getElementById('confirm-filter-form');
      const formData = new FormData(form);
      const spinner = document.getElementById('loading-spinner-confirmar');
      spinner.style.display = 'inline-block';
      fetch('/filtrarOrden', {  // Ajusta el endpoint según tu lógica
        method: 'POST',
        body: formData,
      })
      .then(response => response.text())
      .then(html => {
        spinner.style.display = 'none';
        const parser = new DOMParser();
        const doc = parser.parseFromString(html, 'text/html');
        const newTableBody = doc.querySelector('table tbody');
        document.querySelector('table tbody').innerHTML = newTableBody.innerHTML;
      })
      .catch(error => {
        spinner.style.display = 'none';
        console.error('Error al filtrar:', error);
      });
    });

    // Filtro por tienda, terminal y transacción en Confirmar vía botón
    document.getElementById('filter-button-confirm').addEventListener('click', function () {
    	 // Obtiene el formulario de la pestaña Confirmar
      const form = document.getElementById('confirm-filter-form');
      const formData = new FormData(form);
      const spinner = document.getElementById('loading-spinner-confirmar');
      spinner.style.display = 'inline-block';
      
      // Llama al método del controlador a través del endpoint '/filtrarconfirm'
      fetch('/filtrarConfirm', {  // Ajusta el endpoint según tu lógica
        method: 'POST',
        body: formData,
      })
      .then(response => response.text())
      .then(html => {
        spinner.style.display = 'none';
        const parser = new DOMParser();
        const doc = parser.parseFromString(html, 'text/html');
        const newTableBody = doc.querySelector('table tbody');
        document.querySelector('table tbody').innerHTML = newTableBody.innerHTML;
      })
      .catch(error => {
        spinner.style.display = 'none';
        console.error('Error al filtrar:', error);
      });
    });

    // Función para confirmar el registro seleccionado (se aplica para ambas pestañas)
    // Función para confirmar el registro seleccionado (se aplica para ambas pestañas)
document.getElementById('confirmar-button').addEventListener('click', function () {
  // Deshabilitar el botón inmediatamente después de hacer clic
  this.disabled = true;

  // Buscar la fila seleccionada
  const selectedRow = document.querySelector("table tbody tr.selected");
  if (!selectedRow) {
    alert("Por favor, selecciona un registro");
    this.disabled = false; // Volver a habilitar el botón si no hay selección
    return;
  }
  
  
 


  // Extraer los datos de la fila seleccionada
  const cells = selectedRow.querySelectorAll("td");
  const data = {
    numeroTienda: cells[0]?.innerText,
    terminal: cells[1]?.innerText,
    transaccion: cells[2]?.innerText,
    fecha: cells[3]?.innerText,
    orden: cells[4]?.innerText,
    correo: cells[5]?.innerText,
    total: cells[6]?.innerText,
    confirmacion: cells[7]?.innerText
  };

  // Mostrar el spinner
  const spinner = document.getElementById('loading-spinner-confirmar');
  spinner.style.display = 'inline-block';

  // Realizar la llamada al endpoint /confirmar
  fetch('/confirmar', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data)
  })
  .then(response => response.text())
  .then(result => {
    // Ocultar el spinner después de la respuesta
    spinner.style.display = 'none';

    // Mostrar la respuesta en un contenedor de mensajes
    const mensajeContainer = document.getElementById('mensaje-respuesta');
    if (result.startsWith("Error:")) {
      mensajeContainer.innerHTML = `<div class="alert alert-danger">${result}</div>`;
      this.disabled = false; // Volver a habilitar el botón en caso de error
    } else {
      mensajeContainer.innerHTML = `<div class="alert alert-success">${result}</div>`;

      // Opcional: eliminar la fila confirmada
      selectedRow.classList.remove("selected");
      selectedRow.style.opacity = "0.5"; // Para dar la sensación de que fue confirmado
      selectedRow.remove(); // Borra la fila después de confirmar
    }

    // Después de 10 segundos, borrar el mensaje
    setTimeout(() => {
      mensajeContainer.innerHTML = '';
    }, 10000);
  })
  .catch(error => {
    // Ocultar el spinner en caso de error
    spinner.style.display = 'none';

    // En caso de error en la comunicación
    const mensajeContainer = document.getElementById('mensaje-respuesta');
    mensajeContainer.innerHTML = `<div class="alert alert-danger">Error al enviar la confirmación: ${error}</div>`;
    console.error('Error al enviar la confirmación:', error);

    this.disabled = false; // Habilitar el botón en caso de error
  });
});




   // Establecer la fecha actual en los campos de fecha (Pestaña Filtro)
// Establecer la fecha actual en los campos de fecha (Pestaña Filtro)
document.addEventListener('DOMContentLoaded', function () {
  const fichaInicioInput = document.getElementById('ficha-inicio');
  const fechaFinalInput = document.getElementById('fecha-final');
  const confirmarButton = document.getElementById("confirmar-button");
  
  const today = new Date().toISOString().split('T')[0];
  
  if (fichaInicioInput) {
    fichaInicioInput.value = today;
  }
  
  if (fechaFinalInput) {
    fechaFinalInput.value = today;
  }
  
  // Deshabilitar el botón "Confirmar" inicialmente
  if (confirmarButton) {
    confirmarButton.disabled = true;
  }

  // Función para validar que fecha-final no sea menor que ficha-inicio
  function validarFechas() {
    const fechaInicio = new Date(fichaInicioInput.value);
    const fechaFinal = new Date(fechaFinalInput.value);
    
    if (fechaFinal < fechaInicio) {
      // Si fecha-final es menor que fecha-inicio, mostrar un mensaje de error y deshabilitar el botón
      alert("La fecha final no puede ser menor que la fecha de inicio.");
      if (confirmarButton) {
        confirmarButton.disabled = true;
      }
    } else {
      // Si la fecha final es válida, habilitar el botón "Confirmar"
      if (confirmarButton) {
        confirmarButton.disabled = false;
      }
    }
  }

  // Escuchar los cambios en las fechas
  fichaInicioInput.addEventListener('change', validarFechas);
  fechaFinalInput.addEventListener('change', validarFechas);
});



    // Evitar el envío del formulario al presionar Enter en el campo de Orden (Filtro)
    document.getElementById('orden').addEventListener('keydown', function (event) {
      if (event.key === 'Enter') event.preventDefault();
    });
    // Evitar el envío del formulario al presionar Enter en el campo de Orden (Confirmar)
    document.getElementById('orden-confirm').addEventListener('keydown', function (event) {
      if (event.key === 'Enter') event.preventDefault();
    });

    // Event delegation para seleccionar la fila (solo se seleccionan registros "SIN CONFIRMAR")
    document.querySelector("table tbody").addEventListener("click", function(e) {
  let target = e.target;
  while (target && target.nodeName !== "TR") {
    target = target.parentElement;
  }
  if (target) {
    let headerCells = document.querySelectorAll("table th");
    let estadoIndex = Array.from(headerCells).findIndex(cell => cell.innerText.includes("Estado"));
    const estado = target.cells[estadoIndex] ? target.cells[estadoIndex].innerText.trim() : '';
    
    if (estado !== "SIN CONFIRMAR") {
      const mensajeError = document.getElementById("mensaje-error");
      mensajeError.textContent = "Solo puedes seleccionar registros cuyo estado sea 'SIN CONFIRMAR'.";
      mensajeError.style.display = "block"; // Mostrar el mensaje
      setTimeout(() => mensajeError.style.display = "none", 3000); // Ocultar después de 3 segundos
      return;
    }

    this.querySelectorAll("tr").forEach(row => row.classList.remove("selected"));
    target.classList.add("selected");
    document.getElementById("confirmar-button").disabled = false;
  }
});

document.getElementById("filter-button-confirm").addEventListener("click", function () {
    let terminalInput = document.getElementById("terminal-confirm");
    let transaccionInput = document.getElementById("transaccion-confirm");
    let mensajeError = document.getElementById("mensaje-error");

    // Inicializar el mensaje de error como vacío
    mensajeError.textContent = "";
    mensajeError.style.display = "none";

    // Validar que el campo "Terminal" tenga exactamente 3 dígitos solo si no está vacío
    if (terminalInput.value && terminalInput.value.length !== 3) {
        mensajeError.textContent = "El campo 'Terminal' debe contener exactamente 3 dígitos.";
        mensajeError.style.display = "block";
    }

    // Validar que el campo "Transacción" tenga exactamente 4 dígitos solo si no está vacío
    if (transaccionInput.value && transaccionInput.value.length !== 4) {
        if (mensajeError.textContent !== "") {
            mensajeError.textContent += "\n"; // Agregar salto de línea si ya hay un mensaje
        }
        mensajeError.textContent += "El campo 'Transacción' debe contener exactamente 4 dígitos.";
        mensajeError.style.display = "block";
    }
});



  let currentSort = {};

  function sortTable(columnIndex) {
    const table = document.querySelector('table tbody');
    const rows = Array.from(table.rows);
    const arrow = document.getElementById(`arrow${columnIndex}`);

    const isAscending = currentSort[columnIndex] === 'asc';
    currentSort[columnIndex] = isAscending ? 'desc' : 'asc';

    rows.sort((a, b) => {
      const aText = a.cells[columnIndex].innerText.trim();
      const bText = b.cells[columnIndex].innerText.trim();

      const aValue = isNaN(aText) ? aText : parseFloat(aText);
      const bValue = isNaN(bText) ? bText : parseFloat(bText);

      return (aValue > bValue ? 1 : -1) * (isAscending ? -1 : 1);
    });

    rows.forEach(row => table.appendChild(row));

    // Actualizar flechas
    document.querySelectorAll('.arrow').forEach(span => span.innerHTML = '&#8597;');
    arrow.innerHTML = isAscending ? '&#8593;' : '&#8595;';
  }


document.getElementById('export-button').addEventListener('click', function() {
    document.getElementById('loading-spinner-confirmar').style.display = 'block';

    fetch('/descargarExcel', {
        method: 'POST',
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Error en la respuesta del servidor');
        }
        return response.blob();
    })
    .then(blob => {
        document.getElementById('loading-spinner-confirmar').style.display = 'none';
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = "transacciones.xlsx";
        document.body.appendChild(a);
        a.click();
        a.remove();
    })
    .catch(error => {
        document.getElementById('loading-spinner-confirmar').style.display = 'none';
        console.error('Error al descargar el archivo:', error);
    });
});

