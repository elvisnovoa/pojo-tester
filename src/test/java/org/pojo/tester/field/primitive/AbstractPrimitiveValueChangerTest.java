package org.pojo.tester.field.primitive;


import java.lang.reflect.Field;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import test.fields.AllFiledTypes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AbstractPrimitiveValueChanger.class)
public class AbstractPrimitiveValueChangerTest {

    @Test
    public void Should_Return_False_When_Field_Is_Not_Primitive() throws Exception {
        // given
        final Field field = Thread.class.getDeclaredField("threadQ");

        final AbstractPrimitiveValueChanger<Object> changerMock = mock(AbstractPrimitiveValueChanger.class, CALLS_REAL_METHODS);

        // when
        final boolean result = changerMock.canChange(field);

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void Should_Return_False_When_Class_Is_Not_Compatible_With_Primitive() throws Exception {
        // given
        final Field field = AllFiledTypes.class.getDeclaredField("intType");

        final AbstractPrimitiveValueChanger<Object> changerMock = mock(AbstractPrimitiveValueChanger.class, CALLS_REAL_METHODS);
        doReturn(Object.class).when(changerMock, "getGenericTypeClass");

        // when
        final boolean result = changerMock.canChange(field);

        // then
        assertThat(result).isFalse();
    }

}
