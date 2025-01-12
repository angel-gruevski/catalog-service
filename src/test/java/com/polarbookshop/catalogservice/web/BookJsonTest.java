package com.polarbookshop.catalogservice.web;

import com.polarbookshop.catalogservice.domain.Book;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

@JsonTest
public class BookJsonTest {
    @Autowired
    private JacksonTester<Book> json;

    @Test
    public void testSerialize() throws Exception {
        var book = new Book("1234567890", "Title", "Author", 9.90);
        // When
        JsonContent<Book> jsonContent = json.write(book);

        // Then
        Assertions.assertThat(jsonContent).extractingJsonPathStringValue("@.isbn").isEqualTo(book.isbn());
        Assertions.assertThat(jsonContent).extractingJsonPathStringValue("@.title").isEqualTo(book.title());
        Assertions.assertThat(jsonContent).extractingJsonPathStringValue("@.author").isEqualTo(book.author());
        Assertions.assertThat(jsonContent).extractingJsonPathNumberValue("@.price").isEqualTo(book.price());

    }

    @Test
    void testDeserialize() throws Exception {
        var content = """
                {
                "isbn": "1234567890",
                "title": "Title",
                "author": "Author",
                "price": 9.90
                }
                """;

        Assertions.assertThat(json.parse(content)).usingRecursiveComparison()
                .isEqualTo(new Book("1234567890", "Title", "Author", 9.90));
    }
}
