/**
 * BRUTAL FITNESS - Módulo de Membresías (Frontend JS)
 * Cálculo automático de fechas de vencimiento según el plan seleccionado
 */
document.addEventListener('DOMContentLoaded', function () {
    const inputInicio = document.getElementById('inputFechaInicio');
    const inputVencimiento = document.getElementById('inputFechaVencimiento');
    const selectTipo = document.getElementById('selectTipo');
    const infoVencimiento = document.getElementById('infoVencimiento');

    if (!inputInicio || !inputVencimiento || !selectTipo) {
        return;
    }

    // Inicializar fecha de inicio si está vacía
    if (!inputInicio.value) {
        const today = new Date();
        const yyyy = today.getFullYear();
        const mm = String(today.getMonth() + 1).padStart(2, '0');
        const dd = String(today.getDate()).padStart(2, '0');
        inputInicio.value = `${yyyy}-${mm}-${dd}`;
    }

    function calcularVencimiento() {
        if (!inputInicio.value) return;
        const tipo = selectTipo.value;
        if (!tipo) return;

        const [year, month, day] = inputInicio.value.split('-').map(Number);
        const fecha = new Date(year, month - 1, day);

        if (tipo === 'DIARIO') {
            fecha.setDate(fecha.getDate() + 1);
            if (infoVencimiento) infoVencimiento.textContent = '✨ Válido por 1 día (calculado automáticamente).';
        } else if (tipo === 'SEMANAL') {
            fecha.setDate(fecha.getDate() + 7);
            if (infoVencimiento) infoVencimiento.textContent = '✨ Válido por 1 semana (7 días, calculado automáticamente).';
        } else if (tipo === 'MENSUAL') {
            fecha.setMonth(fecha.getMonth() + 1);
            if (infoVencimiento) infoVencimiento.textContent = '✨ Válido por 1 mes (calculado automáticamente).';
        } else if (tipo === 'TRIMESTRAL') {
            fecha.setMonth(fecha.getMonth() + 3);
            if (infoVencimiento) infoVencimiento.textContent = '✨ Válido por 3 meses (calculado automáticamente).';
        } else if (tipo === 'ANUAL') {
            fecha.setFullYear(fecha.getFullYear() + 1);
            if (infoVencimiento) infoVencimiento.textContent = '✨ Válido por 1 año (calculado automáticamente).';
        } else if (tipo === 'PERSONALIZADA') {
            if (infoVencimiento) infoVencimiento.textContent = '✏️ Modo personalizado: Puedes ingresar la fecha que desees.';
            return;
        }

        const yyyy = fecha.getFullYear();
        const mm = String(fecha.getMonth() + 1).padStart(2, '0');
        const dd = String(fecha.getDate()).padStart(2, '0');
        inputVencimiento.value = `${yyyy}-${mm}-${dd}`;
    }

    selectTipo.addEventListener('change', calcularVencimiento);
    inputInicio.addEventListener('change', calcularVencimiento);

    if (!inputVencimiento.value || selectTipo.value) {
        calcularVencimiento();
    }
});
