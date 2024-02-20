/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import org.mockito.Coverage;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.SuspendMethod;

public class SerializableMethod implements Serializable, MockitoMethod {

    private static final long serialVersionUID = 6005610965006048445L;

    private final Class<?> declaringClass;
    private final String methodName;
    private final Class<?>[] parameterTypes;
    private final Class<?> returnType;
    private final Class<?>[] exceptionTypes;
    private final boolean isVarArgs;
    private final boolean isAbstract;

    private transient volatile Method method;

    public SerializableMethod(Method method) {
        this.method = method;
        declaringClass = method.getDeclaringClass();
        methodName = method.getName();
        parameterTypes = SuspendMethod.trimSuspendParameterTypes(method.getParameterTypes());
        returnType = method.getReturnType();
        exceptionTypes = method.getExceptionTypes();
        isVarArgs = method.isVarArgs();
        isAbstract = (method.getModifiers() & Modifier.ABSTRACT) != 0;
    }

    @Override
    public String getName() {
        return methodName;
    }

    @Override
    public Class<?> getReturnType() {
        return returnType;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    @Override
    public Class<?>[] getExceptionTypes() {
        return exceptionTypes;
    }

    @Override
    public boolean isVarArgs() {
        return isVarArgs;
    }

    @Override
    public boolean isAbstract() {
        return isAbstract;
    }

    @Override
    public Method getJavaMethod() {
        if (method != null) {
            return method;
        }
        try {
            method = declaringClass.getDeclaredMethod(methodName, parameterTypes);
            return method;
        } catch (SecurityException e) {
            String message =
                    String.format(
                            "The method %1$s.%2$s is probably private or protected and cannot be mocked.\n"
                                    + "Please report this as a defect with an example of how to reproduce it.",
                            declaringClass, methodName);
            throw new MockitoException(message, e);
        } catch (NoSuchMethodException e) {
            String message =
                    String.format(
                            "The method %1$s.%2$s does not exists and you should not get to this point.\n"
                                    + "Please report this as a defect with an example of how to reproduce it.",
                            declaringClass, methodName);
            throw new MockitoException(message, e);
        }
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public boolean equals(Object obj) {
        Coverage.setTotalBranches("SerializableMethod::equals", 24);
        Coverage.reached("SerializableMethod::equals", 0);
        if (this == obj) {
            Coverage.reached("SerializableMethod::equals", 1);
            return true;
        } else {
            Coverage.reached("SerializableMethod::equals", 2);
        }
        if (obj == null) {
            Coverage.reached("SerializableMethod::equals", 3);
            return false;
        } else {
            Coverage.reached("SerializableMethod::equals", 4);
        }
        if (getClass() != obj.getClass()) {
            Coverage.reached("SerializableMethod::equals", 5);
            return false;
        } else {
            Coverage.reached("SerializableMethod::equals", 6);
        }
        SerializableMethod other = (SerializableMethod) obj;
        if (declaringClass == null) {
            Coverage.reached("SerializableMethod::equals", 7);
            if (other.declaringClass != null) {
                Coverage.reached("SerializableMethod::equals", 8);
                return false;
            } else {
                Coverage.reached("SerializableMethod::equals", 9);
            }
        } else if (!declaringClass.equals(other.declaringClass)) {
            Coverage.reached("SerializableMethod::equals", 10);
            return false;
        } else {
            Coverage.reached("SerializableMethod::equals", 11);
        }
        if (methodName == null) {
            Coverage.reached("SerializableMethod::equals", 12);
            if (other.methodName != null) {
                Coverage.reached("SerializableMethod::equals", 13);
                return false;
            } else {
                Coverage.reached("SerializableMethod::equals", 14);
            }
        } else if (!methodName.equals(other.methodName)) {
            Coverage.reached("SerializableMethod::equals", 15);
            return false;
        } else {
            Coverage.reached("SerializableMethod::equals", 16);
        }
        if (!Arrays.equals(parameterTypes, other.parameterTypes)) {
            Coverage.reached("SerializableMethod::equals", 17);
            return false;
        } else {
            Coverage.reached("SerializableMethod::equals", 18);
        }
        if (returnType == null) {
            Coverage.reached("SerializableMethod::equals", 19);
            if (other.returnType != null) {
                Coverage.reached("SerializableMethod::equals", 20);
                return false;
            } else {
                Coverage.reached("SerializableMethod::equals", 21);
            }
        } else if (!returnType.equals(other.returnType)) {
            Coverage.reached("SerializableMethod::equals", 22);
            return false;
        } else {
            Coverage.reached("SerializableMethod::equals", 23);
        }
        return true;
    }
}
