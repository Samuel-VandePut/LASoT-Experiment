package eu.stamp_project.reneri.instrumentation;
public class StateObserver {
    private static java.lang.String[] REPLACEMENT;

    private static java.util.regex.Pattern GETTER_NAME = java.util.regex.Pattern.compile("^get[A-Z0-9_].*$");

    private static java.util.regex.Pattern BOOLEAN_GETTER_NAME = java.util.regex.Pattern.compile("^(is|has)[A-Z0-9_].*$");

    static {
        // https://github.com/google/gson/blob/9d44cbc19a73b45971c4ecb33c8d34d673afa210/gson/src/main/java/com/google/gson/stream/JsonWriter.java
        REPLACEMENT = new java.lang.String[128];
        for (int i = 0; i <= 0x1f; i++) {
            REPLACEMENT[i] = java.lang.String.format("\\u%04x", ((int) (i)));
        }
        REPLACEMENT['"'] = "\\\"";
        REPLACEMENT['\\'] = "\\\\";
        REPLACEMENT['\t'] = "\\t";
        REPLACEMENT['\b'] = "\\b";
        REPLACEMENT['\n'] = "\\n";
        REPLACEMENT['\r'] = "\\r";
        REPLACEMENT['\f'] = "\\f";
    }

    private static java.io.Writer output;

    // private static NumberFormat format = NumberFormat.getNumberInstance(Locale.ROOT);
    // private static String currentPoint = "";
    // static String typeToObserve;
    // 
    // private static String SEP = "#";
    private static java.util.HashSet<java.lang.String> wrapperTypes = new java.util.HashSet<>(java.util.Arrays.asList("java.lang.Byte", "java.lang.Short", "java.lang.Integer", "java.lang.Long", "java.lang.Float", "java.lang.Double", "java.lang.Character", "java.lang.Boolean"));

    private static boolean isWrapper(java.lang.Class<?> type) {
        return eu.stamp_project.reneri.instrumentation.StateObserver.wrapperTypes.contains(type.getTypeName());
    }

    private static boolean isAtomic(java.lang.Class<?> type) {
        return ((type == java.lang.String.class) || type.isPrimitive()) || eu.stamp_project.reneri.instrumentation.StateObserver.isWrapper(type);
    }

    protected static void initialize(java.io.Writer writer) {
        eu.stamp_project.reneri.instrumentation.StateObserver.output = writer;
    }

    protected static void initialize() {
        try {
            java.lang.String folder = java.lang.System.getProperty("stamp.reneri.folder");
            if (folder == null) {
                folder = ".";
            }
            java.io.File file = java.nio.file.Paths.get(folder, "observations.jsonl").toFile();
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            eu.stamp_project.reneri.instrumentation.StateObserver.output = new java.io.FileWriter(file);
            // 
            // 
            // final String fullPath = String.format("%s/%s.jsonl", folder, System.currentTimeMillis());
            // File file = new File(fullPath);
            // if(file.getParentFile() != null) {
            // file.getParentFile().mkdirs();
            // }
            // file.createNewFile();
            // /This might produce deadlocks and anyways we are flushing
            eu.stamp_project.reneri.instrumentation.StateObserver.output = new java.io.FileWriter(file);
            /* Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run () { try {output.close();} catch(Exception exc){}}
            });
             */
        } catch (java.io.IOException exc) {
            throw new java.lang.RuntimeException("Could not create the observation file", exc);
        }
    }

    private static void write(java.lang.String text) {
        try {
            if (eu.stamp_project.reneri.instrumentation.StateObserver.output == null)
                eu.stamp_project.reneri.instrumentation.StateObserver.initialize();

            eu.stamp_project.reneri.instrumentation.StateObserver.output.write(text);
            eu.stamp_project.reneri.instrumentation.StateObserver.output.flush();// This harms execution time

        } catch (java.io.IOException exc) {
            throw new java.lang.RuntimeException("Error while writing the observation file", exc);// Stops the test execution

        }
    }

    private static void writeJsonLine(java.lang.String... args) {
        if (args.length == 0) {
            throw new java.lang.AssertionError("Must provide content to write a JSON line");
        }
        if ((args.length % 2) != 0) {
            throw new java.lang.AssertionError("The number of parameters to write a JSON line must be even");
        }
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        builder.append("{\"").append(args[0]).append("\":").append(args[1]);
        for (int index = 2; index < args.length; index += 2) {
            builder.append(",\"").append(args[index]).append("\":").append(args[index + 1]);
        }
        builder.append("}\n");
        eu.stamp_project.reneri.instrumentation.StateObserver.write(builder.toString());
    }

    private static java.lang.String quote(java.lang.String value) {
        return java.lang.String.format("\"%s\"", eu.stamp_project.reneri.instrumentation.StateObserver.escape(value));
    }

    public static void observe(java.lang.String point, java.lang.String type, java.lang.String value) {
        eu.stamp_project.reneri.instrumentation.StateObserver.writeJsonLine("point", eu.stamp_project.reneri.instrumentation.StateObserver.quote(point), "type", eu.stamp_project.reneri.instrumentation.StateObserver.quote(type), "value", value);
    }

    public static void observeNull(java.lang.String point, java.lang.Class<?> type, java.lang.Object value) {
        eu.stamp_project.reneri.instrumentation.StateObserver.writeJsonLine("point", eu.stamp_project.reneri.instrumentation.StateObserver.quote(point), "type", eu.stamp_project.reneri.instrumentation.StateObserver.quote(type.getTypeName()), "null", java.lang.Boolean.toString(value == null));
    }

    public static void observeThrownException(java.lang.String point, java.lang.Throwable exc) {
        eu.stamp_project.reneri.instrumentation.StateObserver.writeJsonLine("point", eu.stamp_project.reneri.instrumentation.StateObserver.quote(point), "exception", eu.stamp_project.reneri.instrumentation.StateObserver.quote(exc.getClass().getTypeName()), "message", eu.stamp_project.reneri.instrumentation.StateObserver.quote(exc.getMessage()));
    }

    public static void observeAtomic(java.lang.String point, java.lang.Class<?> type, java.lang.Object value) {
        if (value instanceof java.lang.String) {
            eu.stamp_project.reneri.instrumentation.StateObserver.observe(point, ((java.lang.String) (value)));
            return;
        } else {
            java.lang.String valueToPrint = (value == null) ? "null" : value.toString();
            if (value instanceof java.lang.Character) {
                // Special case
                valueToPrint = eu.stamp_project.reneri.instrumentation.StateObserver.quote(valueToPrint);
            }
            java.lang.String typeName = type.getTypeName();
            if ((value != null) && (!type.isPrimitive())) {
                typeName = value.getClass().getTypeName();
            }
            eu.stamp_project.reneri.instrumentation.StateObserver.observe(point, typeName, valueToPrint);
        }
    }

    public static <T> T observeState(java.lang.String point, java.lang.Class type, java.lang.Object value) {
        // The type is needed for when the value is null at runtime or it is a primitive type
        eu.stamp_project.reneri.instrumentation.StateObserver.observeBasicState(point, type, value);
        if (value == null) {
            return null;
        }
        if ((!eu.stamp_project.reneri.instrumentation.StateObserver.isAtomic(type)) && (!type.isArray())) {
            eu.stamp_project.reneri.instrumentation.StateObserver.observeInternalState(point, value);
            // observeComputedState(point, value);
        }
        @java.lang.SuppressWarnings("unchecked")
        T result = ((T) (value));
        return result;
    }

    private static boolean mustSkipField(java.lang.reflect.Field field) {
        // Skipping fields instrumented to compute coverage
        java.lang.Class<?> type = field.getType();
        return (field.getName().equals("$jacocoData") && type.isArray()) && type.getComponentType().equals(boolean.class);
    }

    private static void observeInternalState(java.lang.String point, java.lang.Object value) {
        for (java.lang.reflect.Field field : eu.stamp_project.reneri.instrumentation.StateObserver.getFields(value.getClass())) {
            if (eu.stamp_project.reneri.instrumentation.StateObserver.mustSkipField(field)) {
                continue;
            }
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            java.lang.String fieldPoint = (point + "|") + field.getName();
            try {
                java.lang.Object fieldValue = field.get(value);
                eu.stamp_project.reneri.instrumentation.StateObserver.observeBasicState(fieldPoint, field.getType(), fieldValue);
            } catch (java.lang.Throwable exc) {
                eu.stamp_project.reneri.instrumentation.StateObserver.observeThrownException(point, exc);
            }
        }
    }

    private static boolean isAGetter(java.lang.reflect.Method method) {
        if (java.lang.reflect.Modifier.isStatic(method.getModifiers()) || (method.getParameterCount() > 0)) {
            return false;
        }
        java.lang.String name = method.getName();
        return eu.stamp_project.reneri.instrumentation.StateObserver.GETTER_NAME.matcher(name).matches() || (method.getReturnType().equals(boolean.class) && eu.stamp_project.reneri.instrumentation.StateObserver.BOOLEAN_GETTER_NAME.matcher(name).matches());
    }

    private static void observeComputedState(java.lang.String point, java.lang.Object value) {
        for (java.lang.reflect.Method method : value.getClass().getMethods()) {
            if (!eu.stamp_project.reneri.instrumentation.StateObserver.isAGetter(method)) {
                continue;
            }
            java.lang.String pointcut = java.lang.String.format("%s|#%s", point, method.getName());
            try {
                java.lang.Object result = method.invoke(value);
                eu.stamp_project.reneri.instrumentation.StateObserver.observeBasicState(pointcut, method.getReturnType(), result);
            } catch (java.lang.Throwable exc) {
                eu.stamp_project.reneri.instrumentation.StateObserver.observeThrownException(pointcut, exc);
            }
        }
    }

    private static void observeBasicState(java.lang.String point, java.lang.Class type, java.lang.Object value) {
        if (eu.stamp_project.reneri.instrumentation.StateObserver.isAtomic(type)) {
            eu.stamp_project.reneri.instrumentation.StateObserver.observeAtomic(point, type, value);
            return;
        }
        eu.stamp_project.reneri.instrumentation.StateObserver.observeNull(point, type, value);
        if (value == null) {
            return;
        }
        if (type.isArray()) {
            eu.stamp_project.reneri.instrumentation.StateObserver.observe(point + "|#length", java.lang.reflect.Array.getLength(value));
            return;
        }
        java.lang.String sizePointCut = point + "|#size";
        if (value instanceof java.util.Collection) {
            eu.stamp_project.reneri.instrumentation.StateObserver.observe(sizePointCut, ((java.util.Collection) (value)).size());
            return;
        }
        if (value instanceof java.util.Map) {
            eu.stamp_project.reneri.instrumentation.StateObserver.observe(sizePointCut, ((java.util.Map) (value)).size());
            return;
        }
    }

    // Static observers, they will be resolved by the attacher
    public static java.lang.String observe(java.lang.String point, java.lang.String value) {
        eu.stamp_project.reneri.instrumentation.StateObserver.observe(point, "java.lang.String", value == null ? "null" : eu.stamp_project.reneri.instrumentation.StateObserver.quote(value));
        return value;
    }

    public static int observe(java.lang.String point, int value) {
        eu.stamp_project.reneri.instrumentation.StateObserver.observe(point, "int", java.lang.Integer.toString(value));
        return value;
    }

    public static java.lang.Integer observe(java.lang.String point, java.lang.Integer value) {
        eu.stamp_project.reneri.instrumentation.StateObserver.observe(point, "java.lang.Integer", value == null ? "null" : value.toString());
        return value;
    }

    public static boolean observe(java.lang.String point, boolean value) {
        eu.stamp_project.reneri.instrumentation.StateObserver.observe(point, "boolean", java.lang.Boolean.toString(value));
        return value;
    }

    public static java.lang.Boolean observe(java.lang.String point, java.lang.Boolean value) {
        eu.stamp_project.reneri.instrumentation.StateObserver.observe(point, "java.lang.Boolean", java.lang.Boolean.toString(value));
        return value;
    }

    public static byte observe(java.lang.String point, byte value) {
        eu.stamp_project.reneri.instrumentation.StateObserver.observe(point, "byte", java.lang.Byte.toString(value));
        return value;
    }

    public static java.lang.Byte observe(java.lang.String point, java.lang.Byte value) {
        eu.stamp_project.reneri.instrumentation.StateObserver.observe(point, "java.lang.Byte", value == null ? "null" : value.toString());
        return value;
    }

    public static short observe(java.lang.String point, short value) {
        eu.stamp_project.reneri.instrumentation.StateObserver.observe(point, "short", java.lang.Short.toString(value));
        return value;
    }

    public static java.lang.Short observe(java.lang.String point, java.lang.Short value) {
        eu.stamp_project.reneri.instrumentation.StateObserver.observe(point, "java.lang.Short", value == null ? "null" : value.toString());
        return value;
    }

    public static long observe(java.lang.String point, long value) {
        eu.stamp_project.reneri.instrumentation.StateObserver.observe(point, "long", java.lang.Long.toString(value));
        return value;
    }

    public static java.lang.Long observe(java.lang.String point, java.lang.Long value) {
        eu.stamp_project.reneri.instrumentation.StateObserver.observe(point, "java.lang.Long", value == null ? "null" : value.toString());
        return value;
    }

    public static double observe(java.lang.String point, double value) {
        // TODO: Deal with Infinity, -Infinity and Nan
        eu.stamp_project.reneri.instrumentation.StateObserver.observe(point, "double", java.lang.Double.toString(value));
        return value;
    }

    public static float observe(java.lang.String point, float value) {
        eu.stamp_project.reneri.instrumentation.StateObserver.observe(point, "float", java.lang.Float.toString(value));
        return value;
    }

    public static java.lang.Float observe(java.lang.String point, java.lang.Float value) {
        eu.stamp_project.reneri.instrumentation.StateObserver.observe(point, "java.lang.Float", value.toString());
        return value;
    }

    public static java.lang.Double observe(java.lang.String point, java.lang.Double value) {
        // TODO: Deal with Infinity, -Infinity and Nan
        eu.stamp_project.reneri.instrumentation.StateObserver.observe(point, "java.lang.Double", value == null ? "null" : value.toString());
        return value;
    }

    public static char observe(java.lang.String point, char value) {
        eu.stamp_project.reneri.instrumentation.StateObserver.observe(point, "char", eu.stamp_project.reneri.instrumentation.StateObserver.quote(java.lang.Character.toString(value)));
        return value;
    }

    public static java.lang.Character observe(java.lang.String point, java.lang.Character value) {
        eu.stamp_project.reneri.instrumentation.StateObserver.observe(point, "java.lang.Character", value == null ? "null" : eu.stamp_project.reneri.instrumentation.StateObserver.quote(value.toString()));
        return value;
    }

    public static java.lang.String escape(java.lang.String value) {
        java.io.StringWriter result = new java.io.StringWriter(value.length());
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            java.lang.String toWrite = null;
            if (c < eu.stamp_project.reneri.instrumentation.StateObserver.REPLACEMENT.length) {
                toWrite = eu.stamp_project.reneri.instrumentation.StateObserver.REPLACEMENT[c];
                if (toWrite == null) {
                    toWrite = java.lang.Character.toString(c);
                }
            } else if (c == '\u2028') {
                toWrite = "\\u2028";
            } else if (c == '\u2029') {
                toWrite = "\\u2028";
            } else {
                toWrite = java.lang.Character.toString(c);
            }
            result.write(toWrite);
        }
        return result.getBuffer().toString();
    }

    public static class FieldIterator implements java.util.Iterator<java.lang.reflect.Field> {
        private java.lang.Class<?> initialClass;

        private java.lang.Class<?> currentClass;

        private java.lang.reflect.Field[] currentDeclaredFields;

        private int currentFieldIndex = -1;

        public FieldIterator(java.lang.Class<?> klass) {
            this.initialClass = klass;
            currentClass = initialClass;
            currentDeclaredFields = currentClass.getDeclaredFields();
            currentFieldIndex = -1;
        }

        @java.lang.Override
        public boolean hasNext() {
            // Iteration ended
            if (currentDeclaredFields == null) {
                return false;
            }
            // Iterating through the fields of a class
            currentFieldIndex++;
            if (currentFieldIndex < currentDeclaredFields.length) {
                return true;
            }
            do {
                currentClass = currentClass.getSuperclass();
                currentDeclaredFields = (currentClass != null) ? currentClass.getDeclaredFields() : null;
            } while ((currentDeclaredFields != null) && (currentDeclaredFields.length == 0) );
            if (currentDeclaredFields == null) {
                return false;
            }
            currentFieldIndex = 0;
            return true;
        }

        @java.lang.Override
        public java.lang.reflect.Field next() {
            return currentDeclaredFields[currentFieldIndex];
        }
    }

    private static java.lang.Iterable<java.lang.reflect.Field> getFields(java.lang.Class<?> klass) {
        return () -> new eu.stamp_project.reneri.instrumentation.StateObserver.FieldIterator(klass);
    }

    private static java.util.concurrent.atomic.AtomicInteger counter = new java.util.concurrent.atomic.AtomicInteger();

    public static void observe(java.lang.String point, java.lang.StackTraceElement[] stackTrace) {
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        builder.append("{\"point\":").append(eu.stamp_project.reneri.instrumentation.StateObserver.quote(point)).append(", \"trace\":[");
        for (int index = 1; index < stackTrace.length; index++) {
            if (index != 1) {
                builder.append(',');
            }
            java.lang.StackTraceElement element = stackTrace[index];
            builder.append("{\"class\":").append(eu.stamp_project.reneri.instrumentation.StateObserver.quote(element.getClassName())).append(", \"method\":").append(eu.stamp_project.reneri.instrumentation.StateObserver.quote(element.getMethodName())).append(",\"line\":").append(element.getLineNumber()).append("}");
        }
        builder.append("]}\n");
        eu.stamp_project.reneri.instrumentation.StateObserver.write(builder.toString());
    }

    public static void observeProgramState(java.lang.String containerClassName, java.lang.String methodName, java.lang.String signature, java.lang.Class[] parameterTypes, java.lang.Object[] parameters, java.lang.Class thatType, java.lang.Object that, java.lang.Class resultType, java.lang.Object result) {
        synchronized(eu.stamp_project.reneri.instrumentation.StateObserver.counter) {
            java.lang.String pointcut = eu.stamp_project.reneri.instrumentation.StateObserver.counter.getAndIncrement() + "|";
            // There is no evidence that observing the stack trace would produce valuable results,
            // and there are many spurious differences with line numbers lower than 0
            // however, with the stack trace we can identify the exact test method that triggered the execution
            // so we record the stack trace but we don't use it while computing the differences.
            // StackTraceElement[] stackTrace = new Exception().getStackTrace();
            // observe(pointcut + "#trace|length", stackTrace.length - 1);
            // observe(pointcut + "#trace", stackTrace);
            eu.stamp_project.reneri.instrumentation.StateObserver.observeState(pointcut + "#that", thatType, that);
            eu.stamp_project.reneri.instrumentation.StateObserver.observeState(pointcut + "#result", resultType, result);
        }
    }

    private static java.lang.String getMethodCallPointcut(java.lang.String className, java.lang.String methodName, java.lang.String methodDesccription) {
        return java.lang.String.format("%s|%s|%s|%d", className, methodName, methodDesccription, eu.stamp_project.reneri.instrumentation.StateObserver.counter.getAndIncrement());
    }

    public static void observeMethodCall(java.lang.String containerClassName, java.lang.String methodName, java.lang.String methodDescription, java.lang.Class[] parameterTypes, java.lang.Object[] parameters, java.lang.Class thatType, java.lang.Object that, java.lang.Class resultType, java.lang.Object result) {
        synchronized(eu.stamp_project.reneri.instrumentation.StateObserver.counter) {
            java.lang.String pointcut = eu.stamp_project.reneri.instrumentation.StateObserver.getMethodCallPointcut(containerClassName, methodName, methodDescription);
            // There is no evidence that observing the stack trace would produce valuable results,
            // and there are many spurious differences with line numbers lower than 0
            // however, with the stack trace we can identify the exact test method that triggered the execution
            // so we record the stack trace but we don't use it while computing the differences.
            // StackTraceElement[] stackTrace = new Exception().getStackTrace();
            // observe(pointcut + "|#trace|length", stackTrace.length - 1);
            // observe(pointcut + "|#trace", stackTrace);
            for (int index = 0; index < parameters.length; index++) {
                eu.stamp_project.reneri.instrumentation.StateObserver.observeState((pointcut + "|#") + index, parameterTypes[index], parameters[index]);
            }
            if (that != null) {
                eu.stamp_project.reneri.instrumentation.StateObserver.observeState(pointcut + "|#that", thatType, that);
            }
            if ((resultType != void.class) && (resultType != java.lang.Void.class)) {
                eu.stamp_project.reneri.instrumentation.StateObserver.observeState(pointcut + "|#result", resultType, result);
            }
        }
    }

    public static void observeMethodCall(java.lang.String containerClassName, java.lang.String methodName, java.lang.String methodDescription, java.lang.Class[] parameterTypes, java.lang.Object[] parameters, java.lang.Class resultType, java.lang.Object result) {
        eu.stamp_project.reneri.instrumentation.StateObserver.observeMethodCall(containerClassName, methodName, methodDescription, parameterTypes, parameters, null, null, resultType, result);
    }
}