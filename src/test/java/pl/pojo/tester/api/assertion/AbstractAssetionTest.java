package pl.pojo.tester.api.assertion;

import classesForTest.fields.TestEnum1;
import com.google.common.collect.Sets;
import helpers.MapMatcher;
import java.util.Random;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import pl.pojo.tester.api.ConstructorParameters;
import pl.pojo.tester.api.EqualsTester;
import pl.pojo.tester.api.HashCodeTester;
import pl.pojo.tester.internal.assertion.AssertionError;
import pl.pojo.tester.internal.field.AbstractFieldValueChanger;
import pl.pojo.tester.internal.field.DefaultFieldValueChanger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;
import static org.powermock.reflect.Whitebox.getInternalState;
import static org.powermock.reflect.Whitebox.setInternalState;

@RunWith(JUnitPlatform.class)
public class AbstractAssetionTest {

    @Test
    public void Should_Set_Field_Value_Changer() {
        // given
        final AbstractAssetion abstractAssetion = new AbstractAssetionImplementation();
        final AbstractFieldValueChanger expectedFieldsValuesChanger = DefaultFieldValueChanger.INSTANCE;

        // when
        abstractAssetion.using(expectedFieldsValuesChanger);
        final AbstractFieldValueChanger result = getInternalState(abstractAssetion, "abstractFieldValueChanger");

        // then
        assertThat(result).isEqualTo(expectedFieldsValuesChanger);
    }

    @Test
    public void Should_Add_Equals_Tester() {
        // given
        final AbstractAssetion abstractAssetion = new AbstractAssetionImplementation();
        final EqualsTester expectedTester = new EqualsTester();

        // when
        abstractAssetion.testing(Method.EQUALS);

        // then
        assertThat(abstractAssetion.testers).usingRecursiveFieldByFieldElementComparator()
                                            .containsExactly(expectedTester);
    }

    @Test
    public void Should_Add_Equals_And_Hash_Code_Testers() {
        // given
        final AbstractAssetion abstractAssetion = new AbstractAssetionImplementation();
        final EqualsTester expectedTester1 = new EqualsTester();
        final HashCodeTester expectedTester2 = new HashCodeTester();

        // when
        abstractAssetion.testing(Method.EQUALS, Method.HASH_CODE);

        // then
        assertThat(abstractAssetion.testers).usingRecursiveFieldByFieldElementComparator()
                                            .containsExactly(expectedTester1, expectedTester2);
    }

    @Test
    public void Should_Not_Throw_Exception_When_Class_Has_All_Methods_Well_Implemented() {
        // given
        final Class<?> classUnderTest = GoodPojo_Equals_HashCode_ToString.class;

        // when
        final Throwable result = catchThrowable(() -> Assertions.assertPojoMethodsForAll(classUnderTest)
                                                                .areWellImplemented());

        // then
        assertThat(result).isNull();
    }

    @Test
    public void Should_Throw_Exception_When_Class_Has_Method_Implemented_In_Wrong_Way() {
        // given
        final Class<?> classUnderTest = BadPojoEqualsItself.class;

        // when
        final Throwable result = catchThrowable(() -> Assertions.assertPojoMethodsFor(classUnderTest)
                                                                .testing(Method.EQUALS)
                                                                .areWellImplemented());

        // then
        assertThat(result).isInstanceOf(AssertionError.class);
    }

    @Test
    public void Should_Set_Field_Value_Changer_To_Testers() {
        // given
        final AbstractAssetion abstractAssetion = new AbstractAssetionImplementation();
        final AbstractFieldValueChanger expectedFieldsValuesChanger = DefaultFieldValueChanger.INSTANCE;
        final EqualsTester equalsTester = mock(EqualsTester.class);
        setInternalState(abstractAssetion, "testers", Sets.newHashSet(equalsTester));
        abstractAssetion.using(expectedFieldsValuesChanger);

        // when
        abstractAssetion.areWellImplemented();

        // then
        verify(equalsTester, times(1)).setFieldValuesChanger(expectedFieldsValuesChanger);
    }

    @Test
    public void Should_Set_User_Defined_Class_And_Constructor_Paramters_To_Tester() {
        // given
        final AbstractAssetion abstractAssetion = new AbstractAssetionImplementation();
        final EqualsTester equalsTester = mock(EqualsTester.class);
        setInternalState(abstractAssetion, "testers", Sets.newHashSet(equalsTester));
        final Class<String> expectedClass = String.class;
        final Object[] expectedArguments = {'c', 'h', 'a', 'r'};
        final Class[] expectedTypes = {char.class, char.class, char.class, char.class};
        final ConstructorParameters expectedConstructorParameters = new ConstructorParameters(expectedArguments, expectedTypes);
        abstractAssetion.create(expectedClass, expectedConstructorParameters);

        // when
        abstractAssetion.areWellImplemented();

        // then
        verify(equalsTester, times(1)).setUserDefinedConstructors(argThat(new MapMatcher(expectedClass, expectedConstructorParameters)));
    }

    @Test
    public void Should_Call_Next_Create_Method() {
        // given
        final AbstractAssetion abstractAssetion = spy(new AbstractAssetionImplementation());
        final EqualsTester equalsTester = mock(EqualsTester.class);
        setInternalState(abstractAssetion, "testers", Sets.newHashSet(equalsTester));
        final Class<String> expectedClass = String.class;
        final Object[] expectedArguments = {'c', 'h', 'a', 'r'};
        final Class[] expectedTypes = {char.class, char.class, char.class, char.class};
        final ConstructorParameters expectedConstructorParameters = new ConstructorParameters(expectedArguments, expectedTypes);
        abstractAssetion.create(expectedClass, expectedArguments, expectedTypes);

        // when
        abstractAssetion.areWellImplemented();

        // then
        verify(abstractAssetion).create(eq(expectedClass), eq(expectedConstructorParameters));
    }

    @Test
    public void Should_Set_User_Defined_Class_And_Constructor_Paramters_To_Tester_Using_Class_Name() {
        // given
        final AbstractAssetion abstractAssetion = new AbstractAssetionImplementation();
        final EqualsTester equalsTester = mock(EqualsTester.class);
        setInternalState(abstractAssetion, "testers", Sets.newHashSet(equalsTester));
        final Class<?> expectedClass = String.class;
        final Object[] expectedArguments = {'c', 'h', 'a', 'r'};
        final Class[] expectedTypes = {char.class, char.class, char.class, char.class};
        final ConstructorParameters expectedConstructorParameters = new ConstructorParameters(expectedArguments, expectedTypes);
        abstractAssetion.create("java.lang.String", expectedConstructorParameters);

        // when
        abstractAssetion.areWellImplemented();

        // then
        verify(equalsTester, times(1)).setUserDefinedConstructors(argThat(new MapMatcher(expectedClass, expectedConstructorParameters)));
    }

    @Test
    public void Should_Call_Next_Create_Method_Using_Class_Name() {
        // given
        final AbstractAssetion abstractAssetion = spy(new AbstractAssetionImplementation());
        final EqualsTester equalsTester = mock(EqualsTester.class);
        setInternalState(abstractAssetion, "testers", Sets.newHashSet(equalsTester));
        final Object[] expectedArguments = {'c', 'h', 'a', 'r'};
        final Class[] expectedTypes = {char.class, char.class, char.class, char.class};
        final ConstructorParameters expectedConstructorParameters = new ConstructorParameters(expectedArguments, expectedTypes);
        final String expectedClassName = "java.lang.String";
        abstractAssetion.create(expectedClassName, expectedArguments, expectedTypes);

        // when
        abstractAssetion.areWellImplemented();

        // then
        verify(abstractAssetion).create(eq(expectedClassName), eq(expectedConstructorParameters));
    }

    private class AbstractAssetionImplementation extends AbstractAssetion {

        @Override
        protected void testImplementation() {
            // not needed for tests
        }
    }

    private class GoodPojo_Equals_HashCode_ToString {
        public long random;
        public byte byteField;
        public short shortType;
        public int intType;
        public long longType;
        public double doubleType;
        public boolean booleanType;
        public float floatType;
        public char charType;
        public TestEnum1 testEnum1;

        public GoodPojo_Equals_HashCode_ToString() {
            final Random random = new Random();
            this.random = random.nextLong();
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this).append("random", random)
                                            .append("byteField", byteField)
                                            .append("shortType", shortType)
                                            .append("intType", intType)
                                            .append("longType", longType)
                                            .append("doubleType", doubleType)
                                            .append("booleanType", booleanType)
                                            .append("floatType", floatType)
                                            .append("charType", charType)
                                            .append("testEnum1", testEnum1)
                                            .toString();
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            final GoodPojo_Equals_HashCode_ToString that = (GoodPojo_Equals_HashCode_ToString) o;

            return new EqualsBuilder().append(random, that.random)
                                      .append(byteField, that.byteField)
                                      .append(shortType, that.shortType)
                                      .append(intType, that.intType)
                                      .append(longType, that.longType)
                                      .append(doubleType, that.doubleType)
                                      .append(booleanType, that.booleanType)
                                      .append(floatType, that.floatType)
                                      .append(charType, that.charType)
                                      .append(testEnum1, that.testEnum1)
                                      .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder().append(random)
                                        .append(byteField)
                                        .append(shortType)
                                        .append(intType)
                                        .append(longType)
                                        .append(doubleType)
                                        .append(booleanType)
                                        .append(floatType)
                                        .append(charType)
                                        .append(testEnum1)
                                        .toHashCode();
        }

        public long getRandom() {
            return random;
        }

        public void setRandom(final long random) {
            this.random = random;
        }

        public byte getByteField() {
            return byteField;
        }

        public void setByteField(final byte byteField) {
            this.byteField = byteField;
        }

        public short getShortType() {
            return shortType;
        }

        public void setShortType(final short shortType) {
            this.shortType = shortType;
        }

        public int getIntType() {
            return intType;
        }

        public void setIntType(final int intType) {
            this.intType = intType;
        }

        public long getLongType() {
            return longType;
        }

        public void setLongType(final long longType) {
            this.longType = longType;
        }

        public double getDoubleType() {
            return doubleType;
        }

        public void setDoubleType(final double doubleType) {
            this.doubleType = doubleType;
        }

        public boolean isBooleanType() {
            return booleanType;
        }

        public void setBooleanType(final boolean booleanType) {
            this.booleanType = booleanType;
        }

        public float getFloatType() {
            return floatType;
        }

        public void setFloatType(final float floatType) {
            this.floatType = floatType;
        }

        public char getCharType() {
            return charType;
        }

        public void setCharType(final char charType) {
            this.charType = charType;
        }

        public TestEnum1 getTestEnum1() {
            return testEnum1;
        }

        public void setTestEnum1(final TestEnum1 testEnum1) {
            this.testEnum1 = testEnum1;
        }
    }

    class BadPojoEqualsItself {
        private byte byteField;
        private short shortType;
        private int intType;
        private long longType;
        private double doubleType;
        private boolean booleanType;
        private char charType;
        private float floatType;

        @Override
        public String toString() {
            return "";
        }

        @Override
        public boolean equals(final Object o) {
            if (o == null || o.getClass() != getClass()) {
                return false;
            }
            return o != this;
        }

        @Override
        public int hashCode() {
            return 1;
        }

    }

}
