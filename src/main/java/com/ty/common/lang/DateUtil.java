package com.ty.common.lang;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @project hadmin
 * @description desc
 * @auth changtong.ty
 * @date 2014/12/26
 */
public class DateUtil {
    private static final Logger log = LoggerFactory.getLogger(DateUtil.class);
    public static final String LOG_MONITOR_FORMAT = "yyyy-MM-dd hh:mm:ss";
    public static final String QUERY_MONITOR_FORMATE = "yyyy-MM-dd";
    /**
     * PMS 日志解析专用时间格式
     * */
    public static final String PMS_DATE_FORMAT = "yyyy-MM-dd 00";
    public static final String CCM_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";


    private static final Object lockObj = new Object();

    /**
     * 存放不同的日期模板格式的sdf的Map
     */
    private static Map<String, ThreadLocal<SimpleDateFormat>> sdfMap = new HashMap<String, ThreadLocal<SimpleDateFormat>>();

    /**
     * 返回一个ThreadLocal的sdf,每个线程只会new一次sdf
     *
     * @param pattern
     * @return
     */
    private static SimpleDateFormat getSdf(final String pattern) {
        ThreadLocal<SimpleDateFormat> tl = sdfMap.get(pattern);

        // 此处的双重判断和同步是为了防止sdfMap这个单例被多次put重复的sdf
        if (tl == null) {
            synchronized (lockObj) {
                tl = sdfMap.get(pattern);
                if (tl == null) {
                    // 只有Map中还没有这个pattern的sdf才会生成新的sdf并放入map
                    log.info("put new sdf of pattern " + pattern + " to map");

                    // 这里是关键,使用ThreadLocal<SimpleDateFormat>替代原来直接new SimpleDateFormat
                    tl = new ThreadLocal<SimpleDateFormat>() {

                        @Override
                        protected SimpleDateFormat initialValue() {
                            log.info("thread: " + Thread.currentThread() + " init pattern: " + pattern);
                            return new SimpleDateFormat(pattern);
                        }
                    };
                    sdfMap.put(pattern, tl);
                }
            }
        }

        return tl.get();
    }

    /**
     * 是用ThreadLocal<SimpleDateFormat>来获取SimpleDateFormat,这样每个线程只会有一个SimpleDateFormat
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String format(Date date, String pattern) {
        return getSdf(pattern).format(date);
    }

    public static Date parse(String dateStr, String pattern) throws ParseException {
        return getSdf(pattern).parse(dateStr);
    }

    public static String currentTime() {
        return DateUtil.format(new Date(System.currentTimeMillis()),DateUtil.LOG_MONITOR_FORMAT);
    }

    /**
     * <p>Date iterator.</p>
     * @auth changtong.ty
     * @date 2015-07-24
     */
    static class DateIterator implements Iterator<Calendar> {
        private final Calendar endFinal;
        private final Calendar spot;

        /**
         * Constructs a DateIterator that ranges from one date to another.
         *
         * @param startFinal start date (inclusive)
         * @param endFinal   end date (inclusive or not inclusive depend on inclusiveEnd)
         * @param inclusiveEnd inclusive or not inclusive the end date
         */
        DateIterator(Calendar startFinal, Calendar endFinal,boolean inclusiveEnd) {
            super();
            spot = startFinal;
            this.endFinal = endFinal;
            if (DateUtils.truncatedEquals(spot,this.endFinal,Calendar.DATE)) {
                this.endFinal.add(Calendar.DATE , 1);
            } else if (inclusiveEnd){
                this.endFinal.add(Calendar.DATE, 1);
            }
        }
        /**
         * Constructs a DateIterator that ranges from one date to another.
         *
         * @param startFinal start date (inclusive)
         * @param endFinal   end date (not inclusive)
         */
        DateIterator(Calendar startFinal, Calendar endFinal) {
            this(startFinal,endFinal,false);
        }

        /**
         * Constructs a DateIterator that ranges from one date to another.
         *
         * @param startFinal start date (inclusive)
         * @param endFinal   end date (inclusive or not inclusive depend on inclusiveEnd)
         * @param inclusiveEnd inclusive or not inclusive the end date
         */
        DateIterator(Date startFinal, Date endFinal,boolean inclusiveEnd) {
            this(DateUtils.toCalendar(startFinal),DateUtils.toCalendar(endFinal),inclusiveEnd);
        }
        /**
         * Constructs a DateIterator that ranges from one date to another.
         *
         * @param startFinal start date (inclusive)
         * @param endFinal   end date (not inclusive)
         */
        DateIterator(Date startFinal, Date endFinal) {
            this(startFinal,endFinal,false);
        }

        /**
         * Has the iterator not reached the end date yet?
         *
         * @return <code>true</code> if the iterator has yet to reach the end date
         */
        public boolean hasNext() {
            return spot.before(endFinal);
        }

        /**
         * Return the next calendar in the iteration
         *
         * @return Object calendar for the next date
         */
        public Calendar next() {
            if (spot.equals(endFinal)) {
                throw new NoSuchElementException();
            }
            Calendar ret = (Calendar)spot.clone();
            spot.add(Calendar.DATE, 1);
            return ret;
        }

        /**
         * Always throws UnsupportedOperationException.
         *
         * @throws UnsupportedOperationException
         */
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
