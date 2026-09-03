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
        if (ejercicioRepository.count() >= 15) {
            return;
        }

        List<Ejercicio> dataset = new ArrayList<>();

        // 1. PECHO
        dataset.add(Ejercicio.builder()
                .nombre("Press de Banca Plano con Barra")
                .grupoMuscular("Pecho")
                .categoria("Fuerza")
                .equipamiento("Barra Olímpica")
                .nivel("Intermedio")
                .instrucciones("1. Acuéstate en la banca plana con los ojos bajo la barra.\n2. Sujeta la barra con un agarre ligeramente más ancho que los hombros.\n3. Desengancha y baja la barra de forma controlada hacia la parte media del pecho.\n4. Empuja con fuerza hacia arriba extendiendo los brazos sin bloquear los codos.")
                .imagenUrl("https://raw.githubusercontent.com/hasaneyldrm/exercises-dataset/main/exercises/Barbell_Bench_Press_Medium_Grip/0.jpg")
                .gifUrl("https://raw.githubusercontent.com/hasaneyldrm/exercises-dataset/main/exercises/Barbell_Bench_Press_Medium_Grip/animation.gif")
                .build());

        dataset.add(Ejercicio.builder()
                .nombre("Press Inclinado con Mancuernas")
                .grupoMuscular("Pecho")
                .categoria("Hipertrofia")
                .equipamiento("Mancuernas")
                .nivel("Intermedio")
                .instrucciones("1. Ajusta la banca a una inclinación de 30° a 45°.\n2. Sube las mancuernas al pecho con la ayuda de tus rodillas.\n3. Empuja las mancuernas verticalmente hacia arriba juntándolas sin chocar.\n4. Desciende lentamente estirando la porción superior del pectoral.")
                .imagenUrl("https://raw.githubusercontent.com/hasaneyldrm/exercises-dataset/main/exercises/Incline_Dumbbell_Press/0.jpg")
                .gifUrl("https://raw.githubusercontent.com/hasaneyldrm/exercises-dataset/main/exercises/Incline_Dumbbell_Press/animation.gif")
                .build());

        dataset.add(Ejercicio.builder()
                .nombre("Aperturas en Poleas Cruzadas (Cruces de Polea)")
                .grupoMuscular("Pecho")
                .categoria("Aislamiento")
                .equipamiento("Polea Doble")
                .nivel("Principiante")
                .instrucciones("1. Coloca las poleas a la altura de los hombros o alta.\n2. Da un paso adelante manteniendo el torso ligeramente inclinado.\n3. Junta las manos al frente realizando un movimiento de abrazo circular.\n4. Vuelve lentamente controlando la resistencia.")
                .imagenUrl("https://raw.githubusercontent.com/hasaneyldrm/exercises-dataset/main/exercises/Cable_Crossover/0.jpg")
                .gifUrl("https://raw.githubusercontent.com/hasaneyldrm/exercises-dataset/main/exercises/Cable_Crossover/animation.gif")
                .build());

        // 2. ESPALDA
        dataset.add(Ejercicio.builder()
                .nombre("Jalón al Pecho en Polea Alta")
                .grupoMuscular("Espalda")
                .categoria("Hipertrofia")
                .equipamiento("Polea Alta / Lat Machine")
                .nivel("Principiante")
                .instrucciones("1. Siéntate con las piernas ajustadas en los cojines.\n2. Sujeta la barra con agarre prono ancho.\n3. Tira de la barra hacia la parte superior del pecho contrayendo los dorsales.\n4. Controla la fase excéntrica estirando completamente la espalda.")
                .imagenUrl("https://raw.githubusercontent.com/hasaneyldrm/exercises-dataset/main/exercises/Wide_Grip_Lat_Pulldown/0.jpg")
                .gifUrl("https://raw.githubusercontent.com/hasaneyldrm/exercises-dataset/main/exercises/Wide_Grip_Lat_Pulldown/animation.gif")
                .build());

        dataset.add(Ejercicio.builder()
                .nombre("Remo con Barra Inclinado")
                .grupoMuscular("Espalda")
                .categoria("Fuerza")
                .equipamiento("Barra")
                .nivel("Avanzado")
                .instrucciones("1. Inclina el torso hacia adelante unos 45° manteniendo la espalda recta.\n2. Sujeta la barra y jala hacia el ombligo.\n3. Aprieta los omóplatos en la contracción máxima durante 1 segundo.\n4. Baja la barra de forma controlada.")
                .imagenUrl("https://raw.githubusercontent.com/hasaneyldrm/exercises-dataset/main/exercises/Bent_Over_Barbell_Row/0.jpg")
                .gifUrl("https://raw.githubusercontent.com/hasaneyldrm/exercises-dataset/main/exercises/Bent_Over_Barbell_Row/animation.gif")
                .build());

        dataset.add(Ejercicio.builder()
                .nombre("Remo en Polea Baja con Agarre Gironda")
                .grupoMuscular("Espalda")
                .categoria("Hipertrofia")
                .equipamiento("Polea Baja")
                .nivel("Principiante")
                .instrucciones("1. Siéntate con los pies en los apoyos y rodillas ligeramente flexionadas.\n2. Jala el maneral V hacia el abdomen manteniendo la columna neutra.\n3. Retrae las escápulas y regresa suavemente.")
                .imagenUrl("https://raw.githubusercontent.com/hasaneyldrm/exercises-dataset/main/exercises/Seated_Cable_Rows/0.jpg")
                .gifUrl("https://raw.githubusercontent.com/hasaneyldrm/exercises-dataset/main/exercises/Seated_Cable_Rows/animation.gif")
                .build());

        // 3. PIERNAS
        dataset.add(Ejercicio.builder()
                .nombre("Sentadilla Trasera con Barra")
                .grupoMuscular("Piernas")
                .categoria("Fuerza")
                .equipamiento("Barra y Rack")
                .nivel("Avanzado")
                .instrucciones("1. Coloca la barra sobre los trapecios y separa los pies al ancho de hombros.\n2. Desciende empujando las caderas hacia atrás hasta romper el paralelo (90°).\n3. Empuja a través de los talones y sube con la espalda firme.")
                .imagenUrl("https://raw.githubusercontent.com/hasaneyldrm/exercises-dataset/main/exercises/Barbell_Full_Squat/0.jpg")
                .gifUrl("https://raw.githubusercontent.com/hasaneyldrm/exercises-dataset/main/exercises/Barbell_Full_Squat/animation.gif")
                .build());

        dataset.add(Ejercicio.builder()
                .nombre("Prensa de Piernas Inclinada 45°")
                .grupoMuscular("Piernas")
                .categoria("Hipertrofia")
                .equipamiento("Máquina de Prensa")
                .nivel("Principiante")
                .instrucciones("1. Apoya la espalda por completo en el respaldo de la máquina.\n2. Coloca los pies al ancho de hombros en la plataforma.\n3. Desbloquea y baja el peso hasta formar 90° con las rodillas.\n4. Empuja sin bloquear completamente las rodillas al final.")
                .imagenUrl("https://raw.githubusercontent.com/hasaneyldrm/exercises-dataset/main/exercises/Leg_Press/0.jpg")
                .gifUrl("https://raw.githubusercontent.com/hasaneyldrm/exercises-dataset/main/exercises/Leg_Press/animation.gif")
                .build());

        dataset.add(Ejercicio.builder()
                .nombre("Extensión de Cuádriceps en Máquina")
                .grupoMuscular("Piernas")
                .categoria("Aislamiento")
                .equipamiento("Máquina de Extensiones")
                .nivel("Principiante")
                .instrucciones("1. Ajusta la almohadilla sobre la espinilla inferior y apoya la espalda.\n2. Extiende las piernas hacia arriba hasta la horizontal contrayendo los cuádriceps.\n3. Mantén 1 segundo arriba y desciende suavemente.")
                .imagenUrl("https://raw.githubusercontent.com/hasaneyldrm/exercises-dataset/main/exercises/Leg_Extensions/0.jpg")
                .gifUrl("https://raw.githubusercontent.com/hasaneyldrm/exercises-dataset/main/exercises/Leg_Extensions/animation.gif")
                .build());

        dataset.add(Ejercicio.builder()
                .nombre("Curl Femoral Tumbado")
                .grupoMuscular("Piernas")
                .categoria("Aislamiento")
                .equipamiento("Máquina Femoral")
                .nivel("Principiante")
                .instrucciones("1. Tiéndete boca abajo con los tobillos bajo el rodillo acolchado.\n2. Flexiona las piernas llevando los talones hacia los glúteos.\n3. Aguanta la tensión en los isquiosurales y baja lentamente.")
                .imagenUrl("https://raw.githubusercontent.com/hasaneyldrm/exercises-dataset/main/exercises/Lying_Leg_Curls/0.jpg")
                .gifUrl("https://raw.githubusercontent.com/hasaneyldrm/exercises-dataset/main/exercises/Lying_Leg_Curls/animation.gif")
                .build());

        // 4. HOMBROS
        dataset.add(Ejercicio.builder()
                .nombre("Press Militar con Barra de Pie")
                .grupoMuscular("Hombros")
                .categoria("Fuerza")
                .equipamiento("Barra")
                .nivel("Intermedio")
                .instrucciones("1. Coloca la barra a la altura de las clavículas con pies firmes.\n2. Empuja la barra verticalmente por encima de la cabeza apretando el abdomen.\n3. Pasa la cabeza ligeramente hacia adelante al bloquear arriba.\n4. Desciende con control.")
                .imagenUrl("https://raw.githubusercontent.com/hasaneyldrm/exercises-dataset/main/exercises/Standing_Military_Press/0.jpg")
                .gifUrl("https://raw.githubusercontent.com/hasaneyldrm/exercises-dataset/main/exercises/Standing_Military_Press/animation.gif")
                .build());

        dataset.add(Ejercicio.builder()
                .nombre("Elevaciones Laterales con Mancuernas")
                .grupoMuscular("Hombros")
                .categoria("Aislamiento")
                .equipamiento("Mancuernas")
                .nivel("Principiante")
                .instrucciones("1. De pie con mancuernas a los costados y codos ligeramente flexionados.\n2. Eleva los brazos hacia los lados hasta la altura de los hombros.\n3. Vierte las muñecas ligeramente como sirviendo una jarra de agua.\n4. Baja de forma controlada.")
                .imagenUrl("https://raw.githubusercontent.com/hasaneyldrm/exercises-dataset/main/exercises/Side_Lateral_Raise/0.jpg")
                .gifUrl("https://raw.githubusercontent.com/hasaneyldrm/exercises-dataset/main/exercises/Side_Lateral_Raise/animation.gif")
                .build());

        // 5. BRAZOS (BÍCEPS Y TRÍCEPS)
        dataset.add(Ejercicio.builder()
                .nombre("Curl de Bíceps con Barra Z")
                .grupoMuscular("Brazos")
                .categoria("Hipertrofia")
                .equipamiento("Barra Z")
                .nivel("Principiante")
                .instrucciones("1. Sujeta la barra Z por las curvas ergonómicas con agarre supino.\n2. Mantén los codos pegados al cuerpo y flexiona los antebrazos.\n3. Contrae al máximo los bíceps arriba y baja despacio sin balancear la espalda.")
                .imagenUrl("https://raw.githubusercontent.com/hasaneyldrm/exercises-dataset/main/exercises/EZ_Bar_Curl/0.jpg")
                .gifUrl("https://raw.githubusercontent.com/hasaneyldrm/exercises-dataset/main/exercises/EZ_Bar_Curl/animation.gif")
                .build());

        dataset.add(Ejercicio.builder()
                .nombre("Extensiones de Tríceps en Polea con Cuerda")
                .grupoMuscular("Brazos")
                .categoria("Aislamiento")
                .equipamiento("Polea Alta y Cuerda")
                .nivel("Principiante")
                .instrucciones("1. Sujeta los extremos de la cuerda con los codos pegados a los costados.\n2. Empuja hacia abajo y abre los extremos de la cuerda al final del recorrido.\n3. Siente la contracción en la cabeza lateral del tríceps y regresa a 90°.")
                .imagenUrl("https://raw.githubusercontent.com/hasaneyldrm/exercises-dataset/main/exercises/Triceps_Pushdown_Rope_Attachment/0.jpg")
                .gifUrl("https://raw.githubusercontent.com/hasaneyldrm/exercises-dataset/main/exercises/Triceps_Pushdown_Rope_Attachment/animation.gif")
                .build());

        // 6. CORE / ABDOMINALES
        dataset.add(Ejercicio.builder()
                .nombre("Plancha Abdominal Isométrica")
                .grupoMuscular("Core")
                .categoria("Resistencia")
                .equipamiento("Colchoneta / Peso Corporal")
                .nivel("Principiante")
                .instrucciones("1. Apoya los antebrazos y las puntas de los pies en el suelo.\n2. Mantén el cuerpo en línea recta desde los hombros hasta los tobillos.\n3. Activa fuertemente el core y glúteos evitando que la cadera caiga.")
                .imagenUrl("https://raw.githubusercontent.com/hasaneyldrm/exercises-dataset/main/exercises/Plank/0.jpg")
                .gifUrl("https://raw.githubusercontent.com/hasaneyldrm/exercises-dataset/main/exercises/Plank/animation.gif")
                .build());

        for (Ejercicio e : dataset) {
            if (!ejercicioRepository.existsByNombre(e.getNombre())) {
                ejercicioRepository.save(e);
            }
        }
    }
}
