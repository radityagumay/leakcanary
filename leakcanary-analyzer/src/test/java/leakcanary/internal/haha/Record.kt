package leakcanary.internal.haha

import leakcanary.internal.haha.HprofParser.Value
import okio.Buffer
import okio.ByteString
import okio.Okio

sealed class Record {
  class StringRecord(
    val id: Long,
    val string: String
  ) : Record()

  class LoadClassRecord(
    val classSerialNumber: Int,
    val id: Long,
    val stackTraceSerialNumber: Int,
    val classNameStringId: Long
  ) : Record()

  sealed class HeapDumpRecord : Record() {
    abstract val heapId: Int

    class GcRootRecord(
      override val heapId: Int,
      val gcRoot: GcRoot
    ) : HeapDumpRecord()

    data class ClassDumpRecord(
      override val heapId: Int,
      val id: Long,
      val stackTraceSerialNumber: Int,
      val superClassId: Long,
      val classLoaderId: Long,
      val signersId: Long,
      val protectionDomainId: Long,
      val instanceSize: Int,
      val staticFields: List<StaticFieldRecord>,
      val fields: List<FieldRecord>
    ) : HeapDumpRecord() {
      data class StaticFieldRecord(
        val nameStringId: Long,
        val type: Int,
        val value: Value
      )

      data class FieldRecord(
        val nameStringId: Long,
        val type: Int
      )
    }

    class InstanceDumpRecord(
      override val heapId: Int,
      val id: Long,
      val stackTraceSerialNumber: Int,
      val classId: Long,
      val fieldValues: ByteArray
    ) : HeapDumpRecord()

    class ObjectArrayDumpRecord(
      override val heapId: Int,
      id: Long,
      stackTraceSerialNumber: Int,
      arrayClassId: Long,
      elementIds: LongArray
    ) : HeapDumpRecord()

    sealed class PrimitiveArrayDumpRecord : HeapDumpRecord() {
      abstract val id: Long
      abstract val stackTraceSerialNumber: Int

      class BooleanArrayDump(
        override val heapId: Int,
        override val id: Long,
        override val stackTraceSerialNumber: Int,
        val array: BooleanArray
      ) : PrimitiveArrayDumpRecord()

      class CharArrayDump(
        override val heapId: Int,
        override val id: Long,
        override val stackTraceSerialNumber: Int,
        val array: CharArray
      ) : PrimitiveArrayDumpRecord()

      class FloatArrayDump(
        override val heapId: Int,
        override val id: Long,
        override val stackTraceSerialNumber: Int,
        val array: FloatArray
      ) : PrimitiveArrayDumpRecord()

      class DoubleArrayDump(
        override val heapId: Int,
        override val id: Long,
        override val stackTraceSerialNumber: Int,
        val array: DoubleArray
      ) : PrimitiveArrayDumpRecord()

      class ByteArrayDump(
        override val heapId: Int,
        override val id: Long,
        override val stackTraceSerialNumber: Int,
        val array: ByteArray
      ) : PrimitiveArrayDumpRecord()

      class ShortArrayDump(
        override val heapId: Int,
        override val id: Long,
        override val stackTraceSerialNumber: Int,
        val array: ShortArray
      ) : PrimitiveArrayDumpRecord()

      class IntArrayDump(
        override val heapId: Int,
        override val id: Long,
        override val stackTraceSerialNumber: Int,
        val array: IntArray
      ) : PrimitiveArrayDumpRecord()

      class LongArrayDump(
        override val heapId: Int,
        override val id: Long,
        override val stackTraceSerialNumber: Int,
        val array: LongArray
      ) : PrimitiveArrayDumpRecord()
    }

    class HeapDumpInfoRecord(
      override val heapId: Int,
      val heapNameStringId: Long
    ) : HeapDumpRecord()
  }
}