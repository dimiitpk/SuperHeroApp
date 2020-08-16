package com.dimi.superheroapp.util


class ConvertUtils {

    companion object {

        fun fromStringArrayToString(intArray: ArrayList<String>): String {
            val outPut: StringBuilder = StringBuilder()
            for (item in intArray.indices) {
                outPut.append(intArray[item])
                if (item != intArray.lastIndex) outPut.append(",")
            }
            return outPut.toString()
        }

        fun fromStringToStringArray(input: String): ArrayList<String> {
            val outPut = ArrayList<String>()
            if (input.isNotBlank()) {
                if (input.contains(",")) {
                    val stringArray: Array<String> = input.split(",").toTypedArray()
                    for (i in stringArray) {
                        outPut.add(i)
                    }
                } else outPut.add(input)
            }
            return outPut
        }
    }
}