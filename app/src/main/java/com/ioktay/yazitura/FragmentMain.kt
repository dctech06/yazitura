package com.ioktay.yazitura

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.ioktay.yazitura.databinding.FragmentMainBinding

class FragmentMain : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private var secilenZemin = 1//zemin seçilmeden oyuna git e tıklanırsa herzaman tl zemini gider

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        // tl zemin seçen buton tıklama olayı.
        binding.ibTL.setOnClickListener {
            secilenZemin = 1
            secileniGoster()
        }

        // usd zemin seçen buton tıklama olayı.
        binding.ibUSD.setOnClickListener {
            secilenZemin = 2
            secileniGoster()
        }

        //oyun fragment ine gitme olayı
        binding.btOyunGit.setOnClickListener {
            //sayfalar arası geçiş -- oyun sayfasına geçme
            //sayfalar arası veri aktarımı -- secilenZemin
            val gecis = FragmentMainDirections.actionFragmentMainToFragmentGame(secilenZemin)
            Navigation.findNavController(it).navigate(gecis)
        }


        return binding.root
    }

    private fun secileniGoster() {
        // seçilen zemin textView e yazdırılır
        if (secilenZemin == 1){
            binding.txZeminSecimi.text = "TL seçtin"
            //binding.ivMainZemin.setImageResource(R.drawable.zemin_tl)
        }else{
            binding.txZeminSecimi.text = "USD seçtin"
            //binding.ivMainZemin.setImageResource(R.drawable.zemin_dolar)
        }

        // seçilen zemin ToastMessage şeklinde gösterilir
        Toast.makeText(
            requireContext(),
            binding.txZeminSecimi.text,
            Toast.LENGTH_SHORT)
            .show()
    }
}