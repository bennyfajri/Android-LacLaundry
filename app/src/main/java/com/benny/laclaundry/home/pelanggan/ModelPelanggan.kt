package com.benny.laclaundry.home.pelanggan

data class ModelPelanggan(
   val idPelanggan: Int,
   val idUser: Int?,
   val namaPelanggan: String?,
   val nohp: String?,
   val alamat: String?
){
   override fun toString(): String {
      return "$namaPelanggan, $alamat"
   }
}
