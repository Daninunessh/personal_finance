package com.api.personal.finance.presentation.controller;

import com.api.personal.finance.application.usecase.TransactionUseCase;
import com.api.personal.finance.domain.entity.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TransactionUseCase transactionUseCase;

    @InjectMocks
    private TransactionController transactionController;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }

    @Test
    void shouldReturnTransactionsByUserId() throws Exception {
        Transaction transaction = mock(Transaction.class);
        when(transactionUseCase.getTransactionsByUserId(1L)).thenReturn(List.of(transaction));
        mockMvc.perform(get("/users/1/transactions")).andExpect(status().isOk());
        verify(transactionUseCase).getTransactionsByUserId(1L);
    }

    @Test
    void shouldReturnTransactionsByUserIdAndDateBetween() throws Exception {
        Transaction transaction = mock(Transaction.class);
        when(transactionUseCase
                .getTransactionsByUserIdAndTransactionDateBetween(
                        eq(1L),
                        eq(LocalDate.of(2026, 9, 1)),
                        eq(LocalDate.of(2026, 9, 30))))
                .thenReturn(List.of(transaction));

        mockMvc.perform(get("/users/1/transactions")
                .param("startDate", "2026-09-01")
                .param("endDate", "2026-09-30"))
                .andExpect(status().isOk());

        verify(transactionUseCase).getTransactionsByUserIdAndTransactionDateBetween(1L,
                        LocalDate.of(2026, 9, 1),
                        LocalDate.of(2026, 9, 30));
    }

    @Test
    void shouldThrowExceptionWhenOnlyEndDateIsInformed() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> transactionController.getTransactions(1L, null, LocalDate.of(2026, 9, 30)));

        assertEquals("Informe startDate e endDate juntos.", exception.getMessage());
        verifyNoInteractions(transactionUseCase);
    }

    @Test
    void shouldReturnTransactionByIdAndUserId() throws Exception {
        Transaction transaction = mock(Transaction.class);
        when(transactionUseCase.getTransactionByIdAndUserId(10L, 1L)).thenReturn(transaction);
        mockMvc.perform(get("/users/1/transactions/10")).andExpect(status().isOk());
        verify(transactionUseCase).getTransactionByIdAndUserId(10L, 1L);
    }

    @Test
    void shouldCreateTransaction() throws Exception {
        Transaction transaction = mock(Transaction.class);
        when(transaction.getId()).thenReturn(10L);
        when(transactionUseCase.createTransaction(
                eq(1L),
                eq("Compra"),
                eq(new BigDecimal("100.00")),
                eq(LocalDate.of(2026, 9, 15)),
                eq(2L),
                eq(3L)
        )).thenReturn(transaction);
        String json = """
                {
                    "description": "Compra",
                    "amount": 100.00,
                    "transactionDate": "2026-09-15",
                    "accountId": 2,
                    "categoryId": 3
                }
                """;

        mockMvc.perform(post("/users/1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().string(
                        "Location",
                        "http://localhost/users/1/transactions/10"
                ));

        verify(transactionUseCase).createTransaction(1L, "Compra", new BigDecimal("100.00"), LocalDate.of(2026, 9, 15), 2L, 3L);
    }

    @Test
    void shouldUpdateTransaction() throws Exception {
        String json = """
                {
                    "description": "Compra atualizada",
                    "amount": 80.00,
                    "transactionDate": "2026-09-16",
                    "accountId": 2,
                    "categoryId": 3
                }
                """;
        mockMvc.perform(put("/users/1/transactions/10").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isNoContent());
        verify(transactionUseCase).updateTransaction(10L, 1L, "Compra atualizada", new BigDecimal("80.00"), LocalDate.of(2026, 9, 16), 2L, 3L);
    }

    @Test
    void shouldDeleteTransaction() throws Exception {
        mockMvc.perform(delete("/users/1/transactions/10")).andExpect(status().isNoContent());
        verify(transactionUseCase).deleteTransaction(10L, 1L);
    }
}