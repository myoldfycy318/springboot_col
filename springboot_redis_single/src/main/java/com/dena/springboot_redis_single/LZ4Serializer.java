package com.dena.springboot_redis_single;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import net.jpountz.lz4.LZ4BlockInputStream;
import net.jpountz.lz4.LZ4BlockOutputStream;
import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4FastDecompressor;

/**
 * LZ4 压缩 Serializer
 * @author shenyanchao
 */
public class LZ4Serializer implements RedisSerializer<Object> {

    private static final int BUFFER_SIZE = 4096;

    private RedisSerializer<Object> innerSerializer;
    private LZ4FastDecompressor decompresser;
    private LZ4Compressor compressor;

    public LZ4Serializer(RedisSerializer<Object> innerSerializer) {
        this.innerSerializer = innerSerializer;
        LZ4Factory factory = LZ4Factory.fastestInstance();
        this.compressor = factory.fastCompressor();
        this.decompresser = factory.fastDecompressor();
    }

    @Override
    public byte[] serialize(Object graph) throws SerializationException {
        if (graph == null) {
            return new byte[0];
        }
        ByteArrayOutputStream byteOutput = null;
        LZ4BlockOutputStream compressedOutput = null;
        try {
            byte[] bytes = innerSerializer.serialize(graph);
            byteOutput = new ByteArrayOutputStream();
            compressedOutput = new LZ4BlockOutputStream(byteOutput, BUFFER_SIZE, compressor);
            compressedOutput.write(bytes);
            compressedOutput.finish();
            byte[] compressBytes = byteOutput.toByteArray();
            return compressBytes;
        } catch (Exception e) {
            throw new SerializationException("LZ4 Serialization Error", e);
        } finally {
            IOUtils.closeQuietly(compressedOutput);
            IOUtils.closeQuietly(byteOutput);
        }
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {

        if (bytes == null || bytes.length == 0) {
            return null;
        }

        ByteArrayOutputStream baos = null;
        LZ4BlockInputStream lzis = null;
        try {
            baos = new ByteArrayOutputStream(BUFFER_SIZE);
            lzis = new LZ4BlockInputStream(new ByteArrayInputStream(bytes), decompresser);
            int count;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((count = lzis.read(buffer)) != -1) {
                baos.write(buffer, 0, count);
            }
            Object result = innerSerializer.deserialize(baos.toByteArray());
            return result;
        } catch (Exception e) {
            throw new SerializationException("LZ4 deserizelie error", e);
        } finally {
            IOUtils.closeQuietly(lzis);
            IOUtils.closeQuietly(baos);
        }
    }
}