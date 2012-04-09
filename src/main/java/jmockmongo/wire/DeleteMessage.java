/**
 * Copyright (c) 2012, Thilo Planz. All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Apache License, Version 2.0
 * as published by the Apache Software Foundation (the "License").
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 * You should have received a copy of the License along with this program.
 * If not, see <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package jmockmongo.wire;

import static jmockmongo.wire.MessageDecoder.readCString;
import static jmockmongo.wire.MessageDecoder.readDocument;
import static jmockmongo.wire.MessageDecoder.readInt32;

import org.bson.BSONObject;
import org.jboss.netty.buffer.ChannelBuffer;

class DeleteMessage extends Message {

	static final int OP_CODE_DELETE = 2006;

	// struct {
	// MsgHeader header; // standard message header
	// int32 ZERO; // 0 - reserved for future use
	// cstring fullCollectionName; // "dbname.collectionname"
	// int32 flags; // bit vector - see below for details.
	// document selector; // query object. See below for details.
	// }

	private final int flags;

	private final String fullCollectionName;

	private BSONObject query;

	DeleteMessage(ChannelBuffer data) {
		super(data, OP_CODE_DELETE);
		byte[] fourBytes = new byte[4];
		readInt32(data, fourBytes);
		fullCollectionName = readCString(data);
		flags = readInt32(data, fourBytes);
	}

	BSONObject getQuery() {
		if (query == null) {
			byte[] fourBytes = new byte[4];
			query = readDocument(data, fourBytes);
		}
		return query;
	}

	String getFullCollectionName() {
		return fullCollectionName;
	}

	@Override
	public String toString() {
		return super.toString() + " collection: " + fullCollectionName
				+ " query: " + getQuery();
	}

}
