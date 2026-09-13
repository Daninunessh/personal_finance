package com.api.personal.finance.infrastructure.persistence.adapter;

import com.api.personal.finance.domain.entity.Category;
import com.api.personal.finance.domain.entity.CategoryType;
import com.api.personal.finance.domain.entity.User;
import com.api.personal.finance.infrastructure.persistence.entity.CategoryJpaEntity;
import com.api.personal.finance.infrastructure.persistence.entity.UserJpaEntity;
import com.api.personal.finance.infrastructure.persistence.repository.CategoryJpaRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryRepositoryAdapterTest {

    @Mock
    private CategoryJpaRepository categoryJpaRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private CategoryRepositoryAdapter adapter;

    @Test
    void findByUserId_ShouldReturnMappedDomainList() {
        UserJpaEntity userEntity = new UserJpaEntity();
        userEntity.setId(1L);
        userEntity.setName("John Doe");
        userEntity.setEmail("john.doe@example.com");

        CategoryJpaEntity entity = CategoryJpaEntity.builder()
                .id(10L)
                .name("Presente")
                .type(CategoryType.ENTRADA)
                .user(userEntity)
                .build();

        when(categoryJpaRepository.findByUserId(1L)).thenReturn(List.of(entity));
        List<Category> categories = adapter.findByUserId(1L);
        assertEquals(1, categories.size());
        assertEquals(10L, categories.get(0).getId());
    }

    @Test
    void findByIdAndUserId_ShouldReturnOptionalMappedDomain() {
        UserJpaEntity userEntity = new UserJpaEntity();
        userEntity.setId(1L);
        userEntity.setName("John Doe");
        userEntity.setEmail("john.doe@example.com");

        CategoryJpaEntity entity = CategoryJpaEntity.builder()
                .id(10L)
                .name("Presente")
                .type(CategoryType.ENTRADA)
                .user(userEntity)
                .build();

        when(categoryJpaRepository.findByIdAndUserId(10L, 1L)).thenReturn(Optional.of(entity));
        Optional<Category> result = adapter.findByIdAndUserId(10L, 1L);
        assertTrue(result.isPresent());
        assertEquals(10L, result.get().getId());
    }

    @Test
    void save_ShouldSaveAndReturnMappedDomain() {
        User user = User.builder().id(1L).name("John Doe").email("john.doe@example.com").build();
        Category domain = Category.builder()
                .id(10L)
                .name("Presente")
                .type(CategoryType.ENTRADA)
                .user(user)
                .build();

        UserJpaEntity userRef = new UserJpaEntity();
        userRef.setId(1L);
        userRef.setName("John Doe");
        userRef.setEmail("john.doe@example.com");

        CategoryJpaEntity entityToSave = CategoryJpaEntity.builder()
                .id(10L)
                .name("Presente")
                .type(CategoryType.ENTRADA)
                .user(userRef)
                .build();

        when(entityManager.getReference(eq(UserJpaEntity.class), eq(1L))).thenReturn(userRef);
        when(categoryJpaRepository.save(any(CategoryJpaEntity.class))).thenReturn(entityToSave);

        Category saved = adapter.save(domain);

        assertNotNull(saved);
        assertEquals(10L, saved.getId());
        verify(entityManager).getReference(UserJpaEntity.class, 1L);
        verify(categoryJpaRepository).save(any(CategoryJpaEntity.class));
    }

    @Test
    void deleteById_ShouldCallJpaRepositoryDelete() {
        adapter.deleteById(10L);
        verify(categoryJpaRepository).deleteById(10L);
    }
}