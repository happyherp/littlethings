package de.carlos.dumbparse

import java.io.InputStream

class CSVParser:PartialParse<List<List<String>>> {
    override fun parse(input: ParseSource): List<ParseResult<List<List<String>>>> {


        PCharNot(",".toList())

    }

}