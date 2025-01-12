package com.polarbookshop.catalogservice.web;

import com.polarbookshop.catalogservice.domain.BookNotFoundException;
import com.polarbookshop.catalogservice.domain.BookService;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
public class BookControllerMvcTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @Test
    public void whenGetBookNotExistingThenShouldReturn404() throws Exception {

        // Arrange
        String isbn = "73737313940";

        // Given
        BDDMockito.given(bookService.viewBookDetails(isbn)).willThrow(BookNotFoundException.class);

        //When-Then
        mockMvc
                .perform(MockMvcRequestBuilders.get("/books/" + isbn))
                .andExpect(status().isNotFound());
    }
}