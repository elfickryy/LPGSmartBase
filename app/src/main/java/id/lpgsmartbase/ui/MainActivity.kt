package id.lpgsmartbase.ui
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.lpgsmartbase.databinding.ActivityMainBinding
class MainActivity : AppCompatActivity() { override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); setContentView(ActivityMainBinding.inflate(layoutInflater).root) } }
