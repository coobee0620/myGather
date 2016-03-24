package com.ty.common.lang.diagnostic;

import com.ty.common.lang.ObjectUtil;
import com.ty.common.lang.StringUtil;
import com.ty.common.lang.enumeration.IntegerEnum;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @project myGather
 * @description �������Բ�ͳ���߳�ִ��ʱ��Ĺ��ߡ�
 * @auth changtong.ty
 * @date 2015/6/23
 */
public final class Profiler {
    private static final ThreadLocal<Entry> entryStack = new ThreadLocal<Entry>();

    /**
     * ��ʼ��ʱ��
     */
    public static void start() {
        start((String) null);
    }

    /**
     * ��ʼ��ʱ��
     *
     * @param message ��һ��entry����Ϣ
     */
    public static void start(String message) {
        entryStack.set(new Entry(message, null, null));
    }

    /**
     * ��ʼ��ʱ��
     *
     * @param message ��һ��entry����Ϣ
     */
    public static void start(Message message) {
        entryStack.set(new Entry(message, null, null));
    }

    /**
     * �����ʱ����
     *
     * <p>
     * ����Ժ�����ٴε���<code>start</code>�������¼�ʱ��
     * </p>
     */
    public static void reset() {
        entryStack.set(null);
    }

    /**
     * ��ʼһ���µ�entry������ʱ��
     *
     * @param message ��entry����Ϣ
     */
    public static void enter(String message) {
        Entry currentEntry = getCurrentEntry();

        if (currentEntry != null) {
            currentEntry.enterSubEntry(message);
        }
    }

    /**
     * ��ʼһ���µ�entry������ʱ��
     *
     * @param message ��entry����Ϣ
     */
    public static void enter(Message message) {
        Entry currentEntry = getCurrentEntry();

        if (currentEntry != null) {
            currentEntry.enterSubEntry(message);
        }
    }

    /**
     * ���������һ��entry����¼����ʱ�䡣
     */
    public static void release() {
        Entry currentEntry = getCurrentEntry();

        if (currentEntry != null) {
            currentEntry.release();
        }
    }

    /**
     * ȡ�úķѵ���ʱ�䡣
     *
     * @return �ķѵ���ʱ�䣬���δ��ʼ��ʱ���򷵻�<code>-1</code>
     */
    public static long getDuration() {
        Entry entry = entryStack.get();

        if (entry != null) {
            return entry.getDuration();
        } else {
            return -1;
        }
    }

    /**
     * �г����е�entry��
     *
     * @return �г�����entry����ͳ�Ƹ�����ռ�õ�ʱ��
     */
    public static String dump() {
        return dump("", "");
    }

    /**
     * �г����е�entry��
     *
     * @param prefix ǰ׺
     *
     * @return �г�����entry����ͳ�Ƹ�����ռ�õ�ʱ��
     */
    public static String dump(String prefix) {
        return dump(prefix, prefix);
    }

    /**
     * �г����е�entry��
     *
     * @param prefix1 ����ǰ׺
     * @param prefix2 ������ǰ׺
     *
     * @return �г�����entry����ͳ�Ƹ�����ռ�õ�ʱ��
     */
    public static String dump(String prefix1, String prefix2) {
        Entry entry = entryStack.get();

        if (entry != null) {
            return entry.toString(prefix1, prefix2);
        } else {
            return StringUtil.EMPTY_STRING;
        }
    }

    /**
     * ȡ�õ�һ��entry��
     *
     * @return ��һ��entry����������ڣ��򷵻�<code>null</code>
     */
    public static Entry getEntry() {
        return entryStack.get();
    }

    /**
     * ȡ�������һ��entry��
     *
     * @return �����һ��entry����������ڣ��򷵻�<code>null</code>
     */
    private static Entry getCurrentEntry() {
        Entry subEntry = entryStack.get();
        Entry entry = null;

        if (subEntry != null) {
            do {
                entry    = subEntry;
                subEntry = entry.getUnreleasedEntry();
            } while (subEntry != null);
        }

        return entry;
    }

    /**
     * ����һ����ʱ��Ԫ��
     */
    public static final class Entry {
        private final List subEntries  = new ArrayList(4);
        private final Object message;
        private final Entry  parentEntry;
        private final Entry  firstEntry;
        private final long   baseTime;
        private final long   startTime;
        private long         endTime;

        /**
         * ����һ���µ�entry��
         *
         * @param message entry����Ϣ��������<code>null</code>
         * @param parentEntry ��entry��������<code>null</code>
         * @param firstEntry ��һ��entry��������<code>null</code>
         */
        private Entry(Object message, Entry parentEntry, Entry firstEntry) {
            this.message     = message;
            this.startTime   = System.currentTimeMillis();
            this.parentEntry = parentEntry;
            this.firstEntry  = (Entry) ObjectUtil.defaultIfNull(firstEntry, this);
            this.baseTime    = (firstEntry == null) ? 0
                    : firstEntry.startTime;
        }

        /**
         * ȡ��entry����Ϣ��
         */
        public String getMessage() {
            String messageString = null;

            if (message instanceof String) {
                messageString = (String) message;
            } else if (message instanceof Message) {
                Message      messageObject = (Message) message;
                MessageLevel level = MessageLevel.BRIEF_MESSAGE;

                if (isReleased()) {
                    level = messageObject.getMessageLevel(this);
                }

                if (level.equals(MessageLevel.DETAILED_MESSAGE)) {
                    messageString = messageObject.getDetailedMessage();
                } else {
                    messageString = messageObject.getBriefMessage();
                }
            }

            return StringUtil.defaultIfEmpty(messageString, null);
        }

        /**
         * ȡ��entry����ڵ�һ��entry����ʼʱ�䡣
         *
         * @return �����ʼʱ��
         */
        public long getStartTime() {
            return (baseTime > 0) ? (startTime - baseTime)
                    : 0;
        }

        /**
         * ȡ��entry����ڵ�һ��entry�Ľ���ʱ�䡣
         *
         * @return ��Խ���ʱ�䣬���entry��δ�������򷵻�<code>-1</code>
         */
        public long getEndTime() {
            if (endTime < baseTime) {
                return -1;
            } else {
                return endTime - baseTime;
            }
        }

        /**
         * ȡ��entry������ʱ�䡣
         *
         * @return entry������ʱ�䣬���entry��δ�������򷵻�<code>-1</code>
         */
        public long getDuration() {
            if (endTime < startTime) {
                return -1;
            } else {
                return endTime - startTime;
            }
        }

        /**
         * ȡ��entry�������õ�ʱ�䣬����ʱ���ȥ������entry���õ�ʱ�䡣
         *
         * @return entry�������õ�ʱ�䣬���entry��δ�������򷵻�<code>-1</code>
         */
        public long getDurationOfSelf() {
            long duration = getDuration();

            if (duration < 0) {
                return -1;
            } else if (subEntries.isEmpty()) {
                return duration;
            } else {
                for (Object subEntry1 : subEntries) {
                    Entry subEntry = (Entry) subEntry1;

                    duration -= subEntry.getDuration();
                }

                if (duration < 0) {
                    return -1;
                } else {
                    return duration;
                }
            }
        }

        /**
         * ȡ�õ�ǰentry�ڸ�entry����ռ��ʱ��ٷֱȡ�
         *
         * @return �ٷֱ�
         */
        public double getPecentage() {
            double parentDuration = 0;
            double duration = getDuration();

            if ((parentEntry != null) && parentEntry.isReleased()) {
                parentDuration = parentEntry.getDuration();
            }

            if ((duration > 0) && (parentDuration > 0)) {
                return duration / parentDuration;
            } else {
                return 0;
            }
        }

        /**
         * ȡ�õ�ǰentry�ڵ�һ��entry����ռ��ʱ��ٷֱȡ�
         *
         * @return �ٷֱ�
         */
        public double getPecentageOfAll() {
            double firstDuration = 0;
            double duration = getDuration();

            if ((firstEntry != null) && firstEntry.isReleased()) {
                firstDuration = firstEntry.getDuration();
            }

            if ((duration > 0) && (firstDuration > 0)) {
                return duration / firstDuration;
            } else {
                return 0;
            }
        }

        /**
         * ȡ��������entries��
         *
         * @return ������entries���б����ɸ��ģ�
         */
        public List getSubEntries() {
            return Collections.unmodifiableList(subEntries);
        }

        /**
         * ������ǰentry������¼����ʱ�䡣
         */
        private void release() {
            endTime = System.currentTimeMillis();
        }

        /**
         * �жϵ�ǰentry�Ƿ������
         *
         * @return ���entry�Ѿ��������򷵻�<code>true</code>
         */
        private boolean isReleased() {
            return endTime > 0;
        }

        /**
         * ����һ���µ���entry��
         *
         * @param message ��entry����Ϣ
         */
        private void enterSubEntry(Object message) {
            Entry subEntry = new Entry(message, this, firstEntry);

            subEntries.add(subEntry);
        }

        /**
         * ȡ��δ��������entry��
         *
         * @return δ��������entry�����û����entry��������entry���ѽ������򷵻�<code>null</code>
         */
        private Entry getUnreleasedEntry() {
            Entry subEntry = null;

            if (!subEntries.isEmpty()) {
                subEntry = (Entry) subEntries.get(subEntries.size() - 1);

                if (subEntry.isReleased()) {
                    subEntry = null;
                }
            }

            return subEntry;
        }

        /**
         * ��entryת�����ַ����ı�ʾ��
         *
         * @return �ַ�����ʾ��entry
         */
        public String toString() {
            return toString("", "");
        }

        /**
         * ��entryת�����ַ����ı�ʾ��
         *
         * @param prefix1 ����ǰ׺
         * @param prefix2 ������ǰ׺
         *
         * @return �ַ�����ʾ��entry
         */
        private String toString(String prefix1, String prefix2) {
            StringBuffer buffer = new StringBuffer();

            toString(buffer, prefix1, prefix2);

            return buffer.toString();
        }

        /**
         * ��entryת�����ַ����ı�ʾ��
         *
         * @param buffer �ַ���buffer
         * @param prefix1 ����ǰ׺
         * @param prefix2 ������ǰ׺
         */
        private void toString(StringBuffer buffer, String prefix1, String prefix2) {
            buffer.append(prefix1);

            String   message        = getMessage();
            long     startTime      = getStartTime();
            long     duration       = getDuration();
            long     durationOfSelf = getDurationOfSelf();
            double   percent        = getPecentage();
            double   percentOfAll   = getPecentageOfAll();

            Object[] params = new Object[] {
                    message, // {0} - entry��Ϣ
                    startTime, // {1} - ��ʼʱ��
                    duration, // {2} - ������ʱ��
                    durationOfSelf, // {3} - �������ĵ�ʱ��
                    percent, // {4} - �ڸ�entry����ռ��ʱ�����
                    percentOfAll // {5} - ����ʱ�������ɵ�ʱ�����
            };

            StringBuilder pattern = new StringBuilder("{1,number} ");

            if (isReleased()) {
                pattern.append("[{2,number}ms");

                if ((durationOfSelf > 0) && (durationOfSelf != duration)) {
                    pattern.append(" ({3,number}ms)");
                }

                if (percent > 0) {
                    pattern.append(", {4,number,##%}");
                }

                if (percentOfAll > 0) {
                    pattern.append(", {5,number,##%}");
                }

                pattern.append("]");
            } else {
                pattern.append("[UNRELEASED]");
            }

            if (message != null) {
                pattern.append(" - {0}");
            }

            buffer.append(MessageFormat.format(pattern.toString(), params));

            for (int i = 0; i < subEntries.size(); i++) {
                Entry subEntry = (Entry) subEntries.get(i);

                buffer.append('\n');

                if (i == (subEntries.size() - 1)) {
                    subEntry.toString(buffer, prefix2 + "`---", prefix2 + "    "); // ���һ��
                } else if (i == 0) {
                    subEntry.toString(buffer, prefix2 + "+---", prefix2 + "|   "); // ��һ��
                } else {
                    subEntry.toString(buffer, prefix2 + "+---", prefix2 + "|   "); // �м���
                }
            }
        }
    }

    /**
     * ��ʾ��Ϣ�ļ���
     */
    public static final class MessageLevel extends IntegerEnum {
        private static final long        serialVersionUID = 3257849896026388537L;
        public static final MessageLevel NO_MESSAGE       = (MessageLevel) com.ty.alibaba.common.lang.enumeration.Enum.create();
        public static final MessageLevel BRIEF_MESSAGE    = (MessageLevel) com.ty.alibaba.common.lang.enumeration.Enum.create();
        public static final MessageLevel DETAILED_MESSAGE = (MessageLevel) com.ty.alibaba.common.lang.enumeration.Enum.create();
    }

    /**
     * ����һ��profiler entry����ϸ��Ϣ��
     */
    public interface Message {
        MessageLevel getMessageLevel(Entry entry);

        String getBriefMessage();

        String getDetailedMessage();
    }
}
