package com.ty.common.lang.exception;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @project myGather
 * @description desc
 * @auth changtong.ty
 * @date 2015/6/23
 */
public class ExceptionHelper {
    private static final String STRING_EXCEPTION_MESSAGE    = ": ";
    private static final String STRING_CAUSED_BY            = "Caused by: ";
    private static final String STRING_MORE_PREFIX          = "\t... ";
    private static final String STRING_MORE_SUFFIX          = " more";
    private static final String STRING_STACK_TRACE_PREFIX   = "\tat ";
    private static final String STRING_CR                   = "\r";
    private static final String STRING_LF                   = "\n";
    private static final String GET_STACK_TRACE_METHOD_NAME = "getStackTrace";
    private static Method GET_STACK_TRACE_METHOD;

    static {
        // JDK1.4֧��Throwable.getStackTrace()����
        try {
            GET_STACK_TRACE_METHOD = Throwable.class.getMethod(GET_STACK_TRACE_METHOD_NAME,
                    new Class[0]);
        } catch (NoSuchMethodException e) {
        }
    }

    /**
     * ��<code>ChainedThrowable</code>ʵ����ȡ��<code>Throwable</code>����.
     *
     * @param throwable <code>ChainedThrowable</code>ʵ��
     *
     * @return <code>Throwable</code>����
     */
    private static Throwable getThrowable(ChainedThrowable throwable) {
        if (throwable instanceof ChainedThrowableDelegate) {
            return ((ChainedThrowableDelegate) throwable).delegatedThrowable;
        } else {
            return (Throwable) throwable;
        }
    }

    /**
     * ��<code>Throwable</code>ת����<code>ChainedThrowable</code>.
     * ����Ѿ���<code>ChainedThrowable</code>��, ��ֱ�ӷ���,
     * ��������װ��<code>ChainedThrowableDelegate</code>�з���.
     *
     * @param throwable <code>Throwable</code>����
     *
     * @return <code>ChainedThrowable</code>����
     */
    public static ChainedThrowable getChainedThrowable(Throwable throwable) {
        if ((throwable != null) && !(throwable instanceof ChainedThrowable)) {
            return new ChainedThrowableDelegate(throwable);
        }

        return (ChainedThrowable) throwable;
    }

    /**
     * ȡ�ñ�������쳣������, ���������<code>ChainedThrowable</code>,
     * ����<code>ChainedThrowableDelegate</code>��װ������.
     *
     * @param throwable �쳣
     *
     * @return �쳣������
     */
    public static ChainedThrowable getChainedThrowableCause(ChainedThrowable throwable) {
        return getChainedThrowable(throwable.getCause());
    }

    /**
     * ��ӡ����ջ����׼����.
     *
     * @param throwable �쳣
     */
    public static void printStackTrace(ChainedThrowable throwable) {
        printStackTrace(throwable, System.err);
    }

    /**
     * ��ӡ����ջ��ָ�������.
     *
     * @param throwable �쳣
     * @param stream ����ֽ���
     */
    public static void printStackTrace(ChainedThrowable throwable, PrintStream stream) {
        printStackTrace(throwable, new PrintWriter(stream));
    }

    /**
     * ��ӡ����ջ��ָ�������.
     *
     * @param throwable �쳣
     * @param writer ����ַ���
     */
    public static void printStackTrace(ChainedThrowable throwable, PrintWriter writer) {
        synchronized (writer) {
            String[] currentStack = analyzeStackTrace(throwable);

            printThrowableMessage(throwable, writer, false);

            for (int i = 0; i < currentStack.length; i++) {
                writer.println(currentStack[i]);
            }

            printStackTraceRecursive(throwable, writer, currentStack);

            writer.flush();
        }
    }

    /**
     * �ݹ�ش�ӡ�����쳣���ĵ���ջ.
     *
     * @param throwable �쳣
     * @param writer �����
     * @param currentStack ��ǰ�Ķ�ջ
     */
    private static void printStackTraceRecursive(ChainedThrowable throwable, PrintWriter writer,
                                                 String[] currentStack) {
        ChainedThrowable cause = getChainedThrowableCause(throwable);

        if (cause != null) {
            String[] causeStack = analyzeStackTrace(cause);
            int      i = currentStack.length - 1;
            int      j = causeStack.length - 1;

            for (; (i >= 0) && (j >= 0); i--, j--) {
                if (!currentStack[i].equals(causeStack[j])) {
                    break;
                }
            }

            printThrowableMessage(cause, writer, true);

            for (i = 0; i <= j; i++) {
                writer.println(causeStack[i]);
            }

            if (j < (causeStack.length - 1)) {
                writer.println(STRING_MORE_PREFIX + (causeStack.length - j - 1)
                        + STRING_MORE_SUFFIX);
            }

            printStackTraceRecursive(cause, writer, causeStack);
        }
    }

    /**
     * ��ӡ�쳣��message.
     *
     * @param throwable �쳣
     * @param writer �����
     * @param cause �Ƿ��������쳣
     */
    private static void printThrowableMessage(ChainedThrowable throwable, PrintWriter writer,
                                              boolean cause) {
        StringBuffer buffer = new StringBuffer();

        if (cause) {
            buffer.append(STRING_CAUSED_BY);
        }

        Throwable t = getThrowable(throwable);

        buffer.append(t.getClass().getName());

        String message = t.getMessage();

        if (message != null) {
            buffer.append(STRING_EXCEPTION_MESSAGE).append(message);
        }

        writer.println(buffer.toString());
    }

    /**
     * �����쳣�ĵ���ջ, ȡ�õ�ǰ�쳣����Ϣ, �����������쳣����Ϣ.
     *
     * @param throwable ȡ��ָ���쳣�ĵ���ջ
     *
     * @return ����ջ����
     */
    private static String[] analyzeStackTrace(ChainedThrowable throwable) {
        if (GET_STACK_TRACE_METHOD != null) {
            Throwable t = getThrowable(throwable);

            try {
                Object[] stackTrace = (Object[]) GET_STACK_TRACE_METHOD.invoke(t, new Object[0]);
                String[] list = new String[stackTrace.length];

                for (int i = 0; i < stackTrace.length; i++) {
                    list[i] = STRING_STACK_TRACE_PREFIX + stackTrace[i];
                }

                return list;
            } catch (IllegalAccessException e) {
            } catch (IllegalArgumentException e) {
            } catch (InvocationTargetException e) {
            }
        }

        return new StackTraceAnalyzer(throwable).getLines();
    }

    /**
     * ����stack trace�ĸ�����.
     */
    private static class StackTraceAnalyzer {
        private Throwable       throwable;
        private String          message;
        private StackTraceEntry currentEntry  = new StackTraceEntry();
        private StackTraceEntry selectedEntry = currentEntry;
        private StackTraceEntry entry;

        StackTraceAnalyzer(ChainedThrowable throwable) {
            this.throwable     = getThrowable(throwable);
            this.message       = this.throwable.getMessage();

            // ȡ��stack trace�ַ���.
            StringWriter writer = new StringWriter();
            PrintWriter  pw = new PrintWriter(writer);

            throwable.printCurrentStackTrace(pw);

            String stackTraceDump = writer.toString();

            // �ָ��ַ���, ���зָ�, �����ܸmessage�ִ�
            int p = 0;
            int i = -1;
            int j = -1;
            int k = -1;

            while (p < stackTraceDump.length()) {
                boolean includesMessage = false;
                int     s = p;

                if ((i == -1) && (message != null)) {
                    i = stackTraceDump.indexOf(message, p);
                }

                if (j == -1) {
                    j = stackTraceDump.indexOf(STRING_CR, p);
                }

                if (k == -1) {
                    k = stackTraceDump.indexOf(STRING_LF, p);
                }

                // ����ҵ�message
                if ((i != -1) && ((j == -1) || (i <= j)) && ((k == -1) || (i <= k))) {
                    includesMessage     = true;
                    p                   = i + message.length();
                    i                   = -1;

                    if (j < p) {
                        j = -1;
                    }

                    if (k < p) {
                        k = -1;
                    }

                    // ����ֱ������
                }

                // ����ҵ�����CR��CRLF
                if ((j != -1) && ((k == -1) || (j < k))) {
                    p = j + 1;

                    if ((p < stackTraceDump.length())
                            && (stackTraceDump.charAt(p) == STRING_LF.charAt(0))) {
                        p++; // CRLF
                    }

                    addLine(stackTraceDump.substring(s, j), includesMessage);

                    j = -1;

                    if (k < p) {
                        k = -1;
                    }

                    continue;
                }

                // ����ҵ�LF
                if (k != -1) {
                    int q = k + 1;

                    addLine(stackTraceDump.substring(s, k), includesMessage);
                    p     = q;
                    k     = -1;
                    continue;
                }

                // �����û�ҵ�, ˵���������һ��
                int q = stackTraceDump.length();

                if ((p + 1) < q) {
                    addLine(stackTraceDump.substring(s), includesMessage);
                    p = q;
                }
            }

            // ѡ��"��С"��entry
            if (currentEntry.compareTo(selectedEntry) < 0) {
                selectedEntry = currentEntry;
            }
        }

        private void addLine(String line, boolean includesMessage) {
            StackTraceEntry nextEntry = currentEntry.accept(line, includesMessage);

            if (nextEntry != null) {
                // ѡ��"��С"��entry
                if (currentEntry.compareTo(selectedEntry) < 0) {
                    selectedEntry = currentEntry;
                }

                currentEntry = nextEntry;
            }
        }

        String[] getLines() {
            return (String[]) selectedEntry.lines.toArray(new String[selectedEntry.lines.size()]);
        }

        private class StackTraceEntry implements Comparable {
            private List lines             = new ArrayList(10);
            private int  includesMessage   = 0;
            private int  includesThrowable = 0;
            private int  count             = 0;

            StackTraceEntry accept(String line, boolean includesMessage) {
                // �����...at XXX.java(Line...), ����뵽lines�б���.
                // ���򴴽��������µ�entry.
                if (line.startsWith(STRING_STACK_TRACE_PREFIX)) {
                    lines.add(line);
                    count++;
                    return null;
                } else if (count > 0) {
                    StackTraceEntry newEntry = new StackTraceEntry();

                    newEntry.accept(line, includesMessage);
                    return newEntry;
                }

                // ����Ȩ��
                if (includesMessage) {
                    this.includesMessage = 1;
                }

                if (line.indexOf(throwable.getClass().getName()) != -1) {
                    this.includesThrowable = 1;
                }

                return null;
            }

            public int compareTo(Object o) {
                StackTraceEntry otherEntry  = (StackTraceEntry) o;
                int             thisWeight  = includesMessage + includesThrowable;
                int             otherWeight = otherEntry.includesMessage
                        + otherEntry.includesThrowable;

                // weight�������ǰ, ���weight��ͬ, ��countС������ǰ
                if (thisWeight == otherWeight) {
                    return count - otherEntry.count;
                } else {
                    return otherWeight - thisWeight;
                }
            }
        }
    }
}

