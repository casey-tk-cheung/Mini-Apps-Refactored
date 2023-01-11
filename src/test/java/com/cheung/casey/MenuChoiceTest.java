package com.cheung.casey;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static com.cheung.casey.MenuChoice.*;
import static org.junit.jupiter.api.Assertions.*;

class MenuChoiceTest {
    @ParameterizedTest
    @MethodSource("validMenuChoiceProvider")
    void shouldReturnMenuChoiceWhenValid(int option, MenuChoice menuChoice) {
        assertEquals(menuChoice, fromInt(option));
    }

    static Stream<Arguments> validMenuChoiceProvider() {
        return Stream.of(
                Arguments.of(1, KEEP_COUNTING),
                Arguments.of(2, SQUARE_ROOT),
                Arguments.of(3, CHECK_DIGIT_GENERATOR),
                Arguments.of(4, CHECK_DIGIT_CHECKER),
                Arguments.of(9, QUIT)
        );
    }
    @ParameterizedTest
    @ValueSource(ints = {-1,10})
    void shouldReturnInvalidWhenMenuChoiceInvalid(int option) {
        assertEquals(INVALID, fromInt(option));
    }
}