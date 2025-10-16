// Reemplaza el contenido de: /static/js/js.js

// Espera a que todo el contenido del DOM esté cargado
document.addEventListener('DOMContentLoaded', () => {

    // --- Lógica para el menú de navegación (hamburguesa) ---
    const btnNavbar = document.getElementById("btn-navbar");
    if (btnNavbar) {
        btnNavbar.addEventListener("click", () => {
            const menu = document.getElementById("id_ul_navbar");
            if (menu) {
                menu.classList.toggle("active");
            }
        });
    }

    // --- Lógica para la búsqueda de vuelos en index.html ---
    const selectFrom = document.getElementById('select-from');
    const selectTo = document.getElementById('select-to');
    const searchBtn = document.getElementById('search-flight-btn');

    // Si los elementos del formulario de vuelo no existen en la página actual, no continues.
    if (!selectFrom || !selectTo || !searchBtn) {
        return;
    }

    // Función para verificar si se debe mostrar el botón
    const checkSelectors = () => {
        const fromValue = selectFrom.value;
        const toValue = selectTo.value;

        // Muestra el botón solo si ambos selectores tienen un valor
        // y los valores de origen y destino son diferentes.
        if (fromValue && toValue && fromValue !== toValue) {
            searchBtn.classList.remove('hidden');
        } else {
            searchBtn.classList.add('hidden');
        }
    };

    // Añade listeners a ambos selectores para que se verifique cada vez que cambian
    selectFrom.addEventListener('change', checkSelectors);
    selectTo.addEventListener('change', checkSelectors);

    // Añade el listener para el clic en el botón de búsqueda
    searchBtn.addEventListener('click', () => {
        const origin = selectFrom.value;
        const destination = selectTo.value;

        // Construye la URL y redirige a la nueva página
        // El controlador en Spring Boot se encargará de esta ruta
        window.location.href = `/vuelos/buscar?origen=${origin}&destino=${destination}`;
    });

});