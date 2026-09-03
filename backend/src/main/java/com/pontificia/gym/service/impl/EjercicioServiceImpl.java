package com.pontificia.gym.service.impl;

import com.pontificia.gym.entity.Ejercicio;
import com.pontificia.gym.repository.EjercicioRepository;
import com.pontificia.gym.service.EjercicioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EjercicioServiceImpl implements EjercicioService {

    private final EjercicioRepository ejercicioRepository;

    public EjercicioServiceImpl(EjercicioRepository ejercicioRepository) {
        this.ejercicioRepository = ejercicioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ejercicio> listarTodos() {
        return ejercicioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Ejercicio> buscarPorId(Long id) {
        return ejercicioRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ejercicio> buscarPorGrupoMuscular(String grupo) {
        if (grupo == null || grupo.equalsIgnoreCase("TODOS")) {
            return ejercicioRepository.findAll();
        }
        return ejercicioRepository.findByGrupoMuscularIgnoreCase(grupo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ejercicio> buscarPorTexto(String query) {
        if (query == null || query.trim().isEmpty()) {
            return ejercicioRepository.findAll();
        }
        return ejercicioRepository.findByNombreContainingIgnoreCaseOrGrupoMuscularContainingIgnoreCase(query, query);
    }

    @Override
    public Ejercicio guardar(Ejercicio ejercicio) {
        return ejercicioRepository.save(ejercicio);
    }

    @Override
    public void eliminar(Long id) {
        ejercicioRepository.deleteById(id);
    }

    @Override
    public void inicializarDatasetEjercicios() {
        // Limpiamos o resincronizamos si hay registros incompletos
        long validCount = ejercicioRepository.findAll().stream()
                .filter(e -> e.getGifUrl() != null && e.getGifUrl().contains("cdn.jsdelivr.net"))
                .count();

        if (validCount >= 15) {
            return;
        }

        ejercicioRepository.deleteAll();

        List<Ejercicio> dataset = new ArrayList<>();
        String baseCdn = "https://cdn.jsdelivr.net/gh/JahelCuadrado/ExerciseGymGifsDB@v1.1.0/";

        // 1. PECHO
        dataset.add(Ejercicio.builder()
                .nombre("Press de Banca en Máquina Smith")
                .grupoMuscular("Pecho")
                .categoria("Fuerza e Hipertrofia")
                .equipamiento("Máquina Smith")
                .nivel("Intermedio")
                .instrucciones("1. Coloca la banca plana en el centro de la máquina Smith.\n2. Sujeta la barra con un agarre ligeramente más ancho que los hombros.\n3. Desengancha los seguros y desciende la barra hacia el pecho medio de forma controlada.\n4. Empuja con fuerza contrayendo los pectorales sin bloquear los codos.")
                .imagenUrl(baseCdn + "pectorals/smith-wide-grip-bench-press.gif")
                .gifUrl(baseCdn + "pectorals/smith-wide-grip-bench-press.gif")
                .build());

        dataset.add(Ejercicio.builder()
                .nombre("Fondos en Paralelas para Pecho")
                .grupoMuscular("Pecho")
                .categoria("Fuerza")
                .equipamiento("Barras Paralelas")
                .nivel("Intermedio")
                .instrucciones("1. Sujétate en las barras paralelas inclinando el torso ligeramente hacia adelante.\n2. Flexiona los codos bajando hasta que los hombros queden a la altura de los codos (90°).\n3. Empuja hacia arriba con los pectorales hasta la posición inicial.")
                .imagenUrl(baseCdn + "pectorals/weighted-straight-bar-dip.gif")
                .gifUrl(baseCdn + "pectorals/weighted-straight-bar-dip.gif")
                .build());

        dataset.add(Ejercicio.builder()
                .nombre("Flexiones con Manos Abiertas")
                .grupoMuscular("Pecho")
                .categoria("Resistencia")
                .equipamiento("Peso Corporal")
                .nivel("Principiante")
                .instrucciones("1. Coloca las manos en el suelo separadas más allá del ancho de hombros.\n2. Mantén el abdomen tenso y baja el pecho hasta rozar el suelo.\n3. Empuja el piso con fuerza manteniendo el cuerpo en línea recta.")
                .imagenUrl(baseCdn + "pectorals/wide-hand-push-up.gif")
                .gifUrl(baseCdn + "pectorals/wide-hand-push-up.gif")
                .build());

        dataset.add(Ejercicio.builder()
                .nombre("Flexiones Explosivas Superman")
                .grupoMuscular("Pecho")
                .categoria("Pliometría / Potencia")
                .equipamiento("Peso Corporal")
                .nivel("Avanzado")
                .instrucciones("1. Inicia en posición estándar de flexión.\n2. Baja rápido y empuja con máxima potencia despegando manos y pies del suelo.\n3. Extiende los brazos al frente en el aire y aterriza suavemente absorbiendo el impacto.")
                .imagenUrl(baseCdn + "pectorals/superman-push-up.gif")
                .gifUrl(baseCdn + "pectorals/superman-push-up.gif")
                .build());

        // 2. ESPALDA
        dataset.add(Ejercicio.builder()
                .nombre("Remo Inclinado en Máquina Smith")
                .grupoMuscular("Espalda")
                .categoria("Fuerza e Hipertrofia")
                .equipamiento("Máquina Smith")
                .nivel("Intermedio")
                .instrucciones("1. Inclina el torso a 45° manteniendo la espalda completamente recta y pecho erguido.\n2. Sujeta la barra en pronación y jala hacia la parte baja del abdomen.\n3. Aprieta las escápulas 1 segundo y regresa controlando el peso.")
                .imagenUrl(baseCdn + "upper-back/smith-bent-over-row.gif")
                .gifUrl(baseCdn + "upper-back/smith-bent-over-row.gif")
                .build());

        dataset.add(Ejercicio.builder()
                .nombre("Remo Unilateral en Máquina")
                .grupoMuscular("Espalda")
                .categoria("Aislamiento")
                .equipamiento("Máquina Smith / Mancuerna")
                .nivel("Intermedio")
                .instrucciones("1. Apóyate con una mano y jala la carga con el brazo contrario.\n2. Conduce el codo pegado al costado hacia la cadera.\n3. Concéntrate en la contracción del dorsal ancho.")
                .imagenUrl(baseCdn + "upper-back/smith-one-arm-row.gif")
                .gifUrl(baseCdn + "upper-back/smith-one-arm-row.gif")
                .build());

        dataset.add(Ejercicio.builder()
                .nombre("Remo Invertido en Suspensión (TRX)")
                .grupoMuscular("Espalda")
                .categoria("Fuerza Funcional")
                .equipamiento("Correas TRX / Barra Baja")
                .nivel("Principiante")
                .instrucciones("1. Sujeta los manerales inclinándote hacia atrás con talones firmes en el suelo.\n2. Tira del pecho hacia las manos manteniendo el cuerpo rígido como una tabla.\n3. Baja despacio sintiendo el trabajo en los dorsales y romboides.")
                .imagenUrl(baseCdn + "upper-back/suspended-row.gif")
                .gifUrl(baseCdn + "upper-back/suspended-row.gif")
                .build());

        // 3. PIERNAS
        dataset.add(Ejercicio.builder()
                .nombre("Sentadilla Búlgara con Banda / Mancuerna")
                .grupoMuscular("Piernas")
                .categoria("Hipertrofia")
                .equipamiento("Banda / Mancuernas / Banco")
                .nivel("Intermedio")
                .instrucciones("1. Apoya el empeine de un pie en un banco detrás de ti.\n2. Desciende con la pierna delantera flexionando la rodilla a 90°.\n3. Mantén el torso erguido y empuja con el talón de la pierna adelantada para subir.")
                .imagenUrl(baseCdn + "quads/band-one-arm-single-leg-split-squat.gif")
                .gifUrl(baseCdn + "quads/band-one-arm-single-leg-split-squat.gif")
                .build());

        dataset.add(Ejercicio.builder()
                .nombre("Saltos Pliométricos de Potencia")
                .grupoMuscular("Piernas")
                .categoria("Pliometría")
                .equipamiento("Peso Corporal")
                .nivel("Intermedio")
                .instrucciones("1. Flexiona las rodillas cargando energía elástica en los cuádriceps y glúteos.\n2. Salta explosivamente hacia atrás o arriba extendiendo caderas y tobillos.\n3. Aterriza en flexión para amortiguar el impacto.")
                .imagenUrl(baseCdn + "quads/backward-jump.gif")
                .gifUrl(baseCdn + "quads/backward-jump.gif")
                .build());

        // 4. HOMBROS
        dataset.add(Ejercicio.builder()
                .nombre("Press Militar de Hombros")
                .grupoMuscular("Hombros")
                .categoria("Fuerza")
                .equipamiento("Banda Elástica / Mancuernas")
                .nivel("Principiante")
                .instrucciones("1. Sujeta los extremos de la banda o mancuernas a la altura de los hombros.\n2. Empuja hacia arriba por encima de la cabeza hasta extender los brazos.\n3. Baja lentamente controlando la resistencia.")
                .imagenUrl(baseCdn + "delts/band-shoulder-press.gif")
                .gifUrl(baseCdn + "delts/band-shoulder-press.gif")
                .build());

        dataset.add(Ejercicio.builder()
                .nombre("Elevaciones Laterales para Deltoides")
                .grupoMuscular("Hombros")
                .categoria("Aislamiento")
                .equipamiento("Banda / Mancuernas")
                .nivel("Principiante")
                .instrucciones("1. De pie con los brazos a los lados y codos ligeramente flexionados.\n2. Eleva los brazos lateralmente hasta alcanzar la altura de los hombros.\n3. Mantén 1 segundo en la cima y desciende suavemente.")
                .imagenUrl(baseCdn + "delts/band-front-lateral-raise.gif")
                .gifUrl(baseCdn + "delts/band-front-lateral-raise.gif")
                .build());

        dataset.add(Ejercicio.builder()
                .nombre("Pájaros / Vuelos para Deltoides Posterior")
                .grupoMuscular("Hombros")
                .categoria("Aislamiento")
                .equipamiento("Banda / Mancuernas")
                .nivel("Intermedio")
                .instrucciones("1. Inclina el torso al frente manteniendo la espalda recta.\n2. Abre los brazos hacia los costados apretando la parte posterior del hombro.\n3. Regresa despacio sin perder la postura.")
                .imagenUrl(baseCdn + "delts/band-reverse-fly.gif")
                .gifUrl(baseCdn + "delts/band-reverse-fly.gif")
                .build());

        // 5. BRAZOS (BÍCEPS Y TRÍCEPS)
        dataset.add(Ejercicio.builder()
                .nombre("Curl de Bíceps Alterno con Barra")
                .grupoMuscular("Brazos")
                .categoria("Hipertrofia")
                .equipamiento("Barra / Mancuernas")
                .nivel("Principiante")
                .instrucciones("1. De pie con la barra en agarre supino pegada al cuerpo.\n2. Flexiona el codo levantando la carga hacia el pecho contrayendo el bíceps.\n3. Desciende lentamente estirando el brazo por completo.")
                .imagenUrl(baseCdn + "biceps/barbell-alternate-biceps-curl.gif")
                .gifUrl(baseCdn + "biceps/barbell-alternate-biceps-curl.gif")
                .build());

        dataset.add(Ejercicio.builder()
                .nombre("Curl Concentrado de Bíceps")
                .grupoMuscular("Brazos")
                .categoria("Aislamiento")
                .equipamiento("Mancuerna / Banda")
                .nivel("Principiante")
                .instrucciones("1. Siéntate y apoya el codo en la cara interna del muslo.\n2. Flexiona el brazo aislando completamente el bíceps sin mover el torso.\n3. Baja controlando la fase negativa.")
                .imagenUrl(baseCdn + "biceps/band-concentration-curl.gif")
                .gifUrl(baseCdn + "biceps/band-concentration-curl.gif")
                .build());

        // 6. CORE / ABDOMEN
        dataset.add(Ejercicio.builder()
                .nombre("Bicicleta Abdominal (Air Bike Crunch)")
                .grupoMuscular("Core")
                .categoria("Definición y Resistencia")
                .equipamiento("Colchoneta")
                .nivel("Principiante")
                .instrucciones("1. Tiéndete boca arriba con las manos detrás de la nuca y rodillas elevadas a 90°.\n2. Lleva el codo derecho hacia la rodilla izquierda extendiendo la pierna contraria.\n3. Alterna de lado de forma continua y fluida contrayendo los oblicuos.")
                .imagenUrl(baseCdn + "abs/air-bike.gif")
                .gifUrl(baseCdn + "abs/air-bike.gif")
                .build());

        dataset.add(Ejercicio.builder()
                .nombre("Abdominales Sit-Up Clásicos")
                .grupoMuscular("Core")
                .categoria("Fuerza")
                .equipamiento("Colchoneta")
                .nivel("Principiante")
                .instrucciones("1. Tiéndete boca arriba con las rodillas flexionadas y plantas de los pies firmes.\n2. Eleva el torso usando la fuerza de los abdominales hasta quedar sentado.\n3. Desciende vértebra por vértebra sin dejar caer la espalda.")
                .imagenUrl(baseCdn + "abs/3-4-sit-up.gif")
                .gifUrl(baseCdn + "abs/3-4-sit-up.gif")
                .build());

        dataset.add(Ejercicio.builder()
                .nombre("Toques Alternos de Talón para Oblicuos")
                .grupoMuscular("Core")
                .categoria("Aislamiento")
                .equipamiento("Colchoneta")
                .nivel("Principiante")
                .instrucciones("1. Boca arriba con rodillas dobladas y escápulas ligeramente despegadas del suelo.\n2. Inclina el torso lateralmente para tocar el talón derecho con la mano derecha.\n3. Alterna de inmediato al lado izquierdo apretando los oblicuos.")
                .imagenUrl(baseCdn + "abs/alternate-heel-touchers.gif")
                .gifUrl(baseCdn + "abs/alternate-heel-touchers.gif")
                .build());

        ejercicioRepository.saveAll(dataset);
    }
}
