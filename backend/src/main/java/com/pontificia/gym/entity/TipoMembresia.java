package com.pontificia.gym.entity;

import java.time.LocalDate;

public enum TipoMembresia {
    DIARIO {
        @Override
        public LocalDate calcularVencimiento(LocalDate inicio) {
            return inicio.plusDays(1);
        }
    },
    SEMANAL {
        @Override
        public LocalDate calcularVencimiento(LocalDate inicio) {
            return inicio.plusWeeks(1);
        }
    },
    MENSUAL {
        @Override
        public LocalDate calcularVencimiento(LocalDate inicio) {
            return inicio.plusMonths(1);
        }
    },
    TRIMESTRAL {
        @Override
        public LocalDate calcularVencimiento(LocalDate inicio) {
            return inicio.plusMonths(3);
        }
    },
    ANUAL {
        @Override
        public LocalDate calcularVencimiento(LocalDate inicio) {
            return inicio.plusYears(1);
        }
    },
    PERSONALIZADA {
        @Override
        public LocalDate calcularVencimiento(LocalDate inicio) {
            return inicio.plusMonths(1);
        }
    };

    public abstract LocalDate calcularVencimiento(LocalDate inicio);
}
