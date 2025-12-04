/**
 * Travel4U - Formulario de Búsqueda
 * Maneja la interacción del formulario de búsqueda de servicios
 */

document.addEventListener('DOMContentLoaded', () => {
    // Elementos del formulario de búsqueda
    const selectFrom = document.getElementById('select-from');
    const selectTo = document.getElementById('select-to');
    const searchBtn = document.getElementById('search-service-btn');
    const serviceTypeInput = document.getElementById('service-type');
    const searchTitle = document.getElementById('search-title');
    const categoryItems = document.querySelectorAll('.category-item');

    // Si no existen los elementos del formulario, salir
    if (!selectFrom || !selectTo || !searchBtn) {
        return;
    }

    // Configuración de tipos de servicio
    const serviceConfig = {
        'VUELO': { title: 'Busca tu próximo vuelo', button: 'Buscar Vuelos' },
        'CRUCERO': { title: 'Busca tu próximo crucero', button: 'Buscar Cruceros' },
        'BUS': { title: 'Busca tu próximo bus', button: 'Buscar Buses' }
    };

    // Cambiar tipo de servicio desde los botones de categoría
    categoryItems.forEach(item => {
        item.addEventListener('click', (e) => {
            e.preventDefault();
            
            // Actualizar clases activas
            categoryItems.forEach(i => i.classList.remove('active'));
            item.classList.add('active');
            
            // Obtener tipo de servicio
            const tipo = item.dataset.tipo;
            const config = serviceConfig[tipo];
            
            // Actualizar formulario si la configuración existe
            if (config) {
                serviceTypeInput.value = tipo;
                if (searchTitle) searchTitle.textContent = config.title;
                if (searchBtn) searchBtn.textContent = config.button;
            }
        });
    });

    // Validación del formulario de búsqueda
    const checkSelectors = () => {
        const fromValue = selectFrom.value;
        const toValue = selectTo.value;

        // Mostrar botón solo si ambos campos están llenos y son diferentes
        if (fromValue && toValue && fromValue !== toValue) {
            searchBtn.classList.remove('hidden');
        } else {
            searchBtn.classList.add('hidden');
        }
    };

    // Listeners para validación en tiempo real
    selectFrom.addEventListener('change', checkSelectors);
    selectTo.addEventListener('change', checkSelectors);

    // Validación inicial al cargar la página
    checkSelectors();
});