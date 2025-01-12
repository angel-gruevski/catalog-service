package com.polarbookshop.catalogservice;

import com.polarbookshop.catalogservice.domain.Book;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CatalogServiceApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void whenGetRequestWithIdThenBookReturned() {
        var bookIsbn = "1231231230";
        var bookToCreate = new Book(bookIsbn, "Title", "Author", 9.90);

        Book expectedBook = webTestClient.post().uri("/books").bodyValue(bookToCreate).exchange().expectStatus().isCreated().expectBody(Book.class).value(book -> Assertions.assertThat(book).isNotNull()).returnResult().getResponseBody();

        webTestClient.get().uri("/books/" + bookIsbn).exchange().expectStatus().is2xxSuccessful().expectBody(Book.class).value(actualBook -> {
            Assertions.assertThat(actualBook).isNotNull();
            Assertions.assertThat(actualBook.isbn()).isEqualTo(expectedBook.isbn());
        });
    }

    @Test
    void whenPostRequestThenBookCreated() {
        var expectedBook = new Book("1231231231", "Title", "Author", 9.90);

        webTestClient.post().uri("/books").bodyValue(expectedBook).exchange().expectStatus().isCreated().expectBody(Book.class).value(actualBook -> {
            Assertions.assertThat(actualBook).isNotNull();
            Assertions.assertThat(actualBook.isbn()).isEqualTo(expectedBook.isbn());
        });
    }

    @Test
    void whenPutRequestThenBookUpdated() {
        var bookIsbn = "1231231232";
        var bookToCreate = new Book(bookIsbn, "Title", "Author", 9.90);
        Book createdBook = webTestClient.post().uri("/books").bodyValue(bookToCreate).exchange().expectStatus().isCreated().expectBody(Book.class).value(book -> Assertions.assertThat(book).isNotNull()).returnResult().getResponseBody();
        var bookToUpdate = new Book(createdBook.isbn(), createdBook.title(), createdBook.author(), 7.95);

        webTestClient.put().uri("/books/" + bookIsbn).bodyValue(bookToUpdate).exchange().expectStatus().isOk().expectBody(Book.class).value(actualBook -> {
            Assertions.assertThat(actualBook).isNotNull();
            Assertions.assertThat(actualBook.price()).isEqualTo(bookToUpdate.price());
        });
    }

    @Test
    void whenDeleteRequestThenBookDeleted() {
        var bookIsbn = "1231231233";
        var bookToCreate = new Book(bookIsbn, "Title", "Author", 9.90);
        webTestClient.post().uri("/books").bodyValue(bookToCreate).exchange().expectStatus().isCreated();

        webTestClient.delete().uri("/books/" + bookIsbn).exchange().expectStatus().isNoContent();

        webTestClient.get().uri("/books/" + bookIsbn).exchange().expectStatus().isNotFound().expectBody(String.class).value(errorMessage -> Assertions.assertThat(errorMessage).isEqualTo("The book with ISBN " + bookIsbn + " was not found."));
    }
}
