package com.navercorp.pinpoint.collector.grpc.channelz.util;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <a href="http://scrumbucket.org/converting-a-pojo-into-html/">Source</a>
 */
public class HTMLBuilder {


    public static class HTMLStyle extends ToStringStyle {

        private static final String[] toStringClass = {"java.lang", "java.net"};

        public HTMLStyle() {
            setFieldSeparator("</td></tr>" + System.lineSeparator() + "<tr><td>");

            setContentStart("<table border=\"1\" style=\"border-collapse:collapse\">" + System.lineSeparator() +
                    "<thead><tr><th>Field</th><th>Data</th></tr></thead>" +
                    "<tbody><tr><td>");

            setFieldNameValueSeparator("</td><td>");

            setContentEnd("</td></tr>" + System.lineSeparator() + "</tbody></table>");

            setArrayContentDetail(true);
            setUseShortClassName(true);
            setUseClassName(false);
            setUseIdentityHashCode(false);

        }

        @Override
        public void appendDetail(StringBuffer buffer, String fieldName, Object value) {
            for (String prefix : toStringClass) {
                if (value.getClass().getName().startsWith(prefix)) {
                    super.appendDetail(buffer, fieldName, value);
                    return;
                }
            }
            buffer.append(ReflectionToStringBuilder.toString(value, this));
        }

        @Override
        public void append(StringBuffer buffer, String fieldName, Object value, Boolean fullDetail) {
            super.append(buffer, fieldName, value, fullDetail);
        }

        @Override
        protected void appendDetail(StringBuffer buffer, String fieldName, Map<?, ?> map) {
            if (fieldName.equals("others")) {

                List<? extends Map.Entry<?, ?>> list = sort(map);
                appendContentStart(buffer);
                for (Map.Entry<?, ?> entry : list) {
                    String key = (String) entry.getKey();
                    Object value = entry.getValue().toString();

                    append(buffer, key, value, true);
                }
                removeLastFieldSeparator(buffer);
                appendContentEnd(buffer);
                return;
            }
            super.appendDetail(buffer, fieldName, map);
        }

        private List<? extends Map.Entry<?, ?>> sort(Map<?, ?> map) {
            return map.entrySet().stream()
                    .sorted(Comparator.comparing(anotherString -> (String) anotherString.getKey()))
                    .collect(Collectors.toList());
        }
    }

    public String build(Object object) {
        ReflectionToStringBuilder builder = new ReflectionToStringBuilder(object, new HTMLStyle());
        return builder.toString();
    }
}
