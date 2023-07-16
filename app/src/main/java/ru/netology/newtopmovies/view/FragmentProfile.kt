package ru.netology.newtopmovies.view

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import com.hbisoft.pickit.PickiT
import com.hbisoft.pickit.PickiTCallbacks
import ru.netology.newtopmovies.R
import ru.netology.newtopmovies.databinding.FragmentProfileBinding
import ru.netology.newtopmovies.util.Constants
import ru.netology.newtopmovies.viewModel.MovieViewModel
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.ArrayList
import kotlin.system.exitProcess

class FragmentProfile : Fragment(), PickiTCallbacks {

    private val viewModel by activityViewModels<MovieViewModel>()
    private lateinit var pickit: PickiT


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentProfileBinding.inflate(layoutInflater, container, false).also { binding ->

        pickit = PickiT(activity, this, activity)

        viewModel.searchDataSingle.observe(viewLifecycleOwner) { movie ->
            val sizeDataBase = movie.filter { it.rating != 0 }.size
            binding.textView25.text = sizeDataBase.toString()
        }

        binding.apply {
            profileIconAdd.setOnClickListener {
                val alertDialog = AddIdKinopoisk(viewModel, requireContext(), requireView())
                alertDialog.show(parentFragmentManager, "keyOne")
            }

            profileIconShare.setOnClickListener {
                viewModel.shareAllMovie()
            }

            profileIconExport.setOnClickListener {
                val exportListMovies = ExportListMovies(viewModel)
                exportListMovies.show(parentFragmentManager, "ExportListMovies")
            }

            profileIconImport.setOnClickListener {
                importDataBase()
            }
        }

        viewModel.hideToolbar(mapOf("type" to Constants.FRAGMENT_PROFILE, "text" to "Профиль"))
        requireActivity().invalidateOptionsMenu()
    }.root

    @RequiresApi(Build.VERSION_CODES.R)
    private fun importDataBase() {

        if (!Environment.isExternalStorageManager()) {
            val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
            startActivity(intent)
        }

        if (Environment.isExternalStorageManager()) {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "application/octet-stream"
            }
            startActivityForResult(intent, Constants.INTENT_IMPORT)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.INTENT_IMPORT) {
            if (resultCode == Activity.RESULT_OK) {
                pickit.getPath(data?.data, Build.VERSION.SDK_INT)
            }
        }
    }

    override fun PickiTonUriReturned() {
    }

    override fun PickiTonStartListener() {
    }

    override fun PickiTonProgressUpdate(progress: Int) {
    }

    override fun PickiTonCompleteListener(
        path: String?,
        wasDriveFile: Boolean,
        wasUnknownProvider: Boolean,
        wasSuccessful: Boolean,
        Reason: String?
    ) {
        if (path != null) {
            val pathDeleteAndImport = File(activity?.filesDir?.parent.toString() + getString(R.string.path_source))
            val importFile = File(path)
            val deleteAndImportFileDB = File(pathDeleteAndImport, "data_base_movie.db")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Files.deleteIfExists(deleteAndImportFileDB.toPath())
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.d("MyLog", "${importFile.toPath()}")
                Log.d("MyLog", "${deleteAndImportFileDB.toPath()}")
            }

            try {
                importFile.setWritable(true)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Files.copy(
                        importFile.toPath(),
                        deleteAndImportFileDB.toPath(),
                        StandardCopyOption.REPLACE_EXISTING
                    )
                }
                Toast.makeText(activity, "База данных импортирована", Toast.LENGTH_SHORT).show()
                exitProcess(-1)
            } catch (ex: IOException) {
                ex.printStackTrace()
                Toast.makeText(activity, "База данных не импортирована", Toast.LENGTH_SHORT).show()
                Log.d("MyLog", "$ex")
            }
        }
    }

    override fun PickiTonMultipleCompleteListener(
        paths: ArrayList<String>?,
        wasSuccessful: Boolean,
        Reason: String?
    ) {
    }
}