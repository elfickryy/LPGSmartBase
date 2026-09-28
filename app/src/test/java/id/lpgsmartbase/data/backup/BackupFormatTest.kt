package id.lpgsmartbase.data.backup
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
class BackupFormatTest { @Test fun `backup header dan payload valid`() { val out=ByteArrayOutputStream(); DataOutputStream(out).use{it.writeUTF("LPGSMARTBASE");it.writeInt(1);it.writeLong(1);it.writeByte(1)}; assertTrue(BackupManager.isValid(ByteArrayInputStream(out.toByteArray()))) }; @Test fun `payload backup tidak lengkap ditolak`() { val out=ByteArrayOutputStream(); DataOutputStream(out).use{it.writeUTF("LPGSMARTBASE");it.writeInt(1);it.writeLong(2);it.writeByte(1)}; assertFalse(BackupManager.isValid(ByteArrayInputStream(out.toByteArray()))) }; @Test fun `file invalid ditolak`()=assertFalse(BackupManager.isValid(ByteArrayInputStream(byteArrayOf(1,2,3)))) }
