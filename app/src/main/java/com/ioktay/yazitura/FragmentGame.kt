package com.ioktay.yazitura

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ioktay.yazitura.databinding.FragmentGameBinding
import kotlin.random.Random

class FragmentGame : Fragment() {

    private lateinit var binding: FragmentGameBinding
    private var gelenZemin = 1
    private var yazi = true
    private val blinkTime: Long = 80 //dönme efektinin yarı süresi. iki katı tam tur olur.
    private var donguSayisi = 0 // paranın kaç tur döneceği. parayı at buton tıklamasında miktar belirleniyor.

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameBinding.inflate(inflater, container, false)

        init()

        // parayı at buton tıklama olayı
        binding.btParaAt.setOnClickListener {
            binding.txCoin.text = ""
            donguSayisi = 8//para atılınca kaç kez döneceğini belirler
            parayiAt()
        }

        // geri butonu tıklama olayı
        binding.btGeri.setOnClickListener {
            anaMenuyaGit()
        }

        return binding.root
    }

    private fun anaMenuyaGit() {
        //geri butonuna basılınca alertdialog gösterir
        val builder = MaterialAlertDialogBuilder(requireContext())
        builder.setTitle("Ana Menüye dönmek ister misin?")
        builder.setIcon(R.drawable.yazi)

        builder.setPositiveButton("EVET") { dialog, which ->
            //ana menüye dönmek için gerekli kod bloğu
            val gecis = FragmentGameDirections.actionFragmentGameToFragmentMain()
            Navigation.findNavController(binding.btGeri).navigate(gecis)
        }

        builder.setNegativeButton("HAYIR") { dialog, which -> }
        builder.setCancelable(true)
        builder.show()

    }

    private fun parayiAt() {
        // 0 yada 1 den rastgele birini çevirir.
        val random = Random.nextInt(0,2)
        //rastgele sayı döngü sayısına eklenir
        donguSayisi = donguSayisi + random

        donmeEfekti()
    }

    private fun donmeEfekti() {
        //tüm bu fonksiyon for yada while ile daha iyi olurdu
        //aceleden böyle oldu
        val para = binding.ivCoin

        //paranın daralarak küçülme efekti
        val animScaleX1 = ObjectAnimator.ofFloat(para, "scaleX", 0f, 1f).apply {
            //duration = blinkTime
            interpolator = AccelerateInterpolator()
        }

        //parayı tekrar genişleten kod bloğu
        val animScaleX2 = ObjectAnimator.ofFloat(para, "scaleX", 1f, 0f).apply {
            //duration = blinkTime
            interpolator = DecelerateInterpolator()
        }

        AnimatorSet().apply {
            duration = blinkTime
            playSequentially(animScaleX1, animScaleX2)
        }.start()

        //para tamamen görünmez olduğunda diğer yüzünü çeviren kodlar
        yazi = !yazi
        if (yazi){
            binding.ivCoin.setImageResource(R.drawable.yazi)
        }else{
            binding.ivCoin.setImageResource(R.drawable.tura)
        }

        //her turda döngü sayısı 1 eksiltilir
        donguSayisi--

        //efektlerin donguSayisi adedince sırayla görünür olması için verilen gecikme
        //ObjectAnimator da kullanılabilirdi. aceleden böyle oldu
        object : CountDownTimer(blinkTime*2, blinkTime) {
            override fun onFinish() {
                if (donguSayisi > 0){
                    //donguSayisi 0 olana kadar efekt yeniden başlatılır
                    donmeEfekti()
                }else{
                    para.animate()
                        .scaleX(1f)
                        .setDuration(blinkTime)
                        .setInterpolator(AccelerateInterpolator())
                        .start()

                    //donguSayisi 0 olduğunda gelen yüzün yazdırılması
                    binding.txCoin.text = if (yazi){
                        "YAZI"
                    }else{
                        "TURA"
                    }
                }
            }

            override fun onTick(p0: Long) {
            }
        }.start()
    }

    private fun init() {
        //fragmentMain den gelen zemin bilgisini yakalama
        try {
            val bundle: FragmentGameArgs by navArgs()
            gelenZemin = bundle.zemin
        }catch (e:Exception){
            Log.e("getExtra", e.toString())
        }

        binding.txCoin.text = ""
        secilenZeminiGoster()
    }

    private fun secilenZeminiGoster() {
        //zemin resmini atama
        binding.ivGameMain.setBackgroundResource(if (gelenZemin == 1){
            R.drawable.zemin_tl
        }else{
            R.drawable.zemin_dolar
        })
    }
}