package com.navercorp.pinpoint.common.server.bo.serializer.trace.v2;

import com.navercorp.pinpoint.common.hbase.HbaseTables;
import com.navercorp.pinpoint.common.server.bo.SpanBo;
import com.navercorp.pinpoint.common.server.bo.serializer.HbaseSerializer;
import com.navercorp.pinpoint.common.server.bo.serializer.SerializationContext;
import org.apache.hadoop.hbase.client.Put;

import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * @author Woonduk Kang(emeroad)
 */
public class SpanSerializerV2 implements HbaseSerializer<SpanBo, Put> {


    private final SpanEncoder spanEncoder;

    public SpanSerializerV2(SpanEncoder spanEncoder) {
        this.spanEncoder = Objects.requireNonNull(spanEncoder, "spanEncoder");
    }


    @Override
    public void serialize(SpanBo spanBo, Put put, SerializationContext context) {

        final SpanEncodingContext<SpanBo> encodingContext = new SpanEncodingContext<>(spanBo);

        ByteBuffer qualifier = spanEncoder.encodeSpanQualifier(encodingContext);
        ByteBuffer columnValue = spanEncoder.encodeSpanColumnValue(encodingContext);

        long acceptedTime = put.getTimestamp();
        put.addColumn(HbaseTables.TRACE_V2_SPAN.getName(), qualifier, acceptedTime, columnValue);
    }



}
