/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.api.internal.artifacts.ivyservice.modulecache;

import org.gradle.internal.component.model.ModuleSource;
import org.gradle.internal.serialize.Decoder;
import org.gradle.internal.serialize.DefaultSerializer;
import org.gradle.internal.serialize.Encoder;
import org.gradle.internal.serialize.Serializer;

import java.math.BigInteger;

class ModuleDescriptorCacheEntrySerializer implements Serializer<ModuleDescriptorCacheEntry> {
    private final DefaultSerializer<ModuleSource> moduleSourceSerializer = new DefaultSerializer<ModuleSource>(ModuleSource.class.getClassLoader());

    public void write(Encoder encoder, ModuleDescriptorCacheEntry value) throws Exception {
        encoder.writeByte(value.type);
        switch (value.type) {
            case ModuleDescriptorCacheEntry.TYPE_MISSING:
                encoder.writeLong(value.createTimestamp);
                break;
            case ModuleDescriptorCacheEntry.TYPE_PRESENT:
                encoder.writeBoolean(value.isChanging);
                encoder.writeLong(value.createTimestamp);
                moduleSourceSerializer.write(encoder, value.moduleSource);
                byte[] hash = value.moduleDescriptorHash.toByteArray();
                encoder.writeBinary(hash);
                break;
            default:
                throw new IllegalArgumentException("Don't know how to serialize meta-data entry: " + value);
        }
    }

    public ModuleDescriptorCacheEntry read(Decoder decoder) throws Exception {
        byte type = decoder.readByte();
        switch (type) {
            case ModuleDescriptorCacheEntry.TYPE_MISSING:
                long createTimestamp = decoder.readLong();
                return new MissingModuleCacheEntry(createTimestamp);
            case ModuleDescriptorCacheEntry.TYPE_PRESENT:
                boolean isChanging = decoder.readBoolean();
                createTimestamp = decoder.readLong();
                ModuleSource moduleSource = moduleSourceSerializer.read(decoder);
                byte[] encodedHash = decoder.readBinary();
                BigInteger hash = new BigInteger(encodedHash);
                return new ModuleDescriptorCacheEntry(ModuleDescriptorCacheEntry.TYPE_PRESENT, isChanging, createTimestamp, hash, moduleSource);
            default:
                throw new IllegalArgumentException("Don't know how to deserialize meta-data entry of type " + type);
        }
    }
}
