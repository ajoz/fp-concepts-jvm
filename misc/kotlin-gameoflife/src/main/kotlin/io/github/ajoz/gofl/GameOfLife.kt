package io.github.ajoz.gofl

// names heave meaning! names bring meaning to the code
// for the algorithm to be readable, names need several iterations
// we read more code then we write
// we need to perform the same procedures for code as we do for writing books
// reading a text several times helps to check if the meaning is still there
// if the reading flow is there
// for better reading the same things need different names, to show and express intent

// there is a position in the world
// Do we really need to create a specialized class right from the start?
// data class Position(val x: Int, val y: Int)
// maybe we can just do a simplification
typealias Position = Pair<Int, Int>

// would be nice to use more domain specific names instead of first and second
val Position.x: Int
    get() = first

val Position.y: Int
    get() = second

// if the cell does not have any other properties then being alive it means that essentially its a position in the world
// but in certain context its better to think about it using the word Cell not a word Position
typealias Cell = Position

// One generation of live cells is just basically a list, but a word List brings different properties with itself
// its better to think about a generation, list is an implementation detail
typealias Generation = Collection<Cell>

// we want to change each generation into next generation, we need a set of candidates for it
// the word candidates brings our intention with it, it also expresses our uncertainty about it beeing a cell or not
// it's basically a position but reads better as Candidate in correct context
typealias Candidate = Position

// we want to hide the detail of how candidates are stored, also we want to express that we return multiple instances of
// a candidate, we don't need to know its a list or any other data structure
typealias Candidates = Collection<Candidate>

// cell has several positions around it, those positions are it's neighbours. This word brings familiarity of the concept
// we all have neighbours, it describes where the thing is without any values, you feel that it means something that is
// close
typealias Neighbour = Position
typealias Neighbours = Collection<Neighbour>

// maybe its a wrong word, in physics it means how many times per second a thing occurs
// here we would like to express the NeighbourCount for a particular Candidate
typealias Frequency = Pair<Candidate, NeighbourCount>
typealias NeighbourCount = Int

val Frequency.candidate: Candidate
    get() = first

val Frequency.neighbourCount: NeighbourCount
    get() = second

typealias Frequencies = Collection<Frequency>

// getNeighbours :: Cell -> Neighbours
fun getNeighbours(cell: Cell): Neighbours = listOf(
        Neighbour(cell.x - 1, cell.y + 1), Neighbour(cell.x, cell.y + 1), Neighbour(cell.x + 1, cell.y + 1),
        Neighbour(cell.x - 1, cell.y), /*Neighbour(cell.x, cell.y), */ Neighbour(cell.x + 1, cell.y),
        Neighbour(cell.x - 1, cell.y - 1), Neighbour(cell.x, cell.y - 1), Neighbour(cell.x + 1, cell.y - 1)
)

// getCandidates :: Generation -> Candidates
fun  getCandidates(generation: Generation): Candidates =
        generation.flatMap { getNeighbours(it) }

// for a set of candidates
// sort the candidates by their position
// group the candidates by the position
// change each group to a frequency where:
//    frequency consists of a candidate (position)
//    frequency consists of group size (neighbour count)

// getFrequencies :: Candidates -> Frequencies
fun getFrequencies(candidates: Candidates): Frequencies =
        candidates.sortedBy(Candidate::hashCode)
                  .groupBy(Candidate::hashCode)
                  .map { Frequency(it.value[0], it.value.size) }

// frequencyToCell :: Frequency -> Cell
fun frequencyToCell(freq: Frequency): Cell = freq.candidate

//// isCandidateACell :: Frequency -> Generation -> Boolean
//fun isCandidateACell(freq: Frequency, generation: Generation): Boolean =
//        (freq.neighbourCount == 3) || (freq.neighbourCount == 2 && generation.contains(freq.candidate))

// isCandidateACell :: Generation -> Frequency -> Boolean
fun isCandidateACell(gen: Generation): (Frequency) -> Boolean = { freq ->
    (freq.neighbourCount == 3) || (freq.neighbourCount == 2 && gen.contains(freq.candidate))
}

// a nice example of currying
fun findAliveCells(generation: Generation): (Frequencies) -> Generation =
        { fs -> fs.filter(isCandidateACell(generation)).map { frequencyToCell(it) } }

// for a generation of cells
// get all candidates from the cells neighbours
// get how frequent a candidate cell is a neighbour
// each candidate that has three neighbours or two neighbours and is already a cell goes to next generation
// discard other candidates

// getNextGeneration :: Generation -> Generation
fun getNextGeneration(generation: Generation): Generation {
    val candidates = getCandidates(generation)
    val frequencies = getFrequencies(candidates)
    return findAliveCells(generation)(frequencies)
}

// power that Kotlin has over what Java can give us:
infix fun <A, R1, R2> ((A) -> R1).andThen(other: (R1) -> R2): (A) -> R2 = { a ->
    other(this(a))
}

fun getNextGeneration2(generation: Generation): Generation =
        (::getCandidates andThen ::getFrequencies andThen findAliveCells(generation))(generation)

val getNextGeneration4 = { gen: Generation ->
    (::getCandidates andThen ::getFrequencies andThen findAliveCells(gen))(gen)
}

fun getNextGeneration5(generation: Generation,
                       findAlive: (Frequencies) -> Generation = findAliveCells(generation)): Generation {
    return (::getCandidates andThen ::getFrequencies andThen findAlive)(generation)
}

fun getNextGeneration6(generation: Generation) =
        findAliveCells(generation)(getFrequencies(getCandidates(generation)))

fun <A, B, T> weave(f: (A) -> B): (T) -> (A) -> B = { g ->
    { a ->
        f(a)
    }
}

fun <A, B> weaveG(f: (A) -> B): (Generation) -> (A) -> B = weave(f)

infix fun <A, B, C, T> ((T) -> (A) -> B).then(f: (T) -> (B) -> C): (T) -> (A) -> C = { g ->
    { a ->
        f(g)(this(g)(a))
    }
}

fun <T> squish(f: (T) -> (T) -> T): (T) -> (T) = { g -> f(g)(g) }

val getCandidatesG = weaveG(::getCandidates)
val getFrequenciesG = weaveG(::getFrequencies)
val findAliveCellsG: (Generation) -> (Frequencies) -> Generation = ::findAliveCells

val getNextGeneration3 =
        squish(getCandidatesG then getFrequenciesG then findAliveCellsG)

// impure -- returns Unit, a very broad type would like more precised type like haskell IO
// printGeneration :: Generation -> IO (String)
fun printGeneration(generation: Generation): Unit {
    if (generation.isEmpty()) println("[empty]")
    else {
        val minX = generation.minBy { cell -> cell.x }?.x ?: 0
        val maxX = generation.maxBy { cell -> cell.x }?.x ?: 0
        val minY = generation.minBy { cell -> cell.y }?.y ?: 0
        val maxY = generation.maxBy { cell -> cell.y }?.y ?: 0

        (minX..maxX).forEach { x ->
            (minY..maxY).forEach { y ->
                if (generation.contains(Cell(x, y)))
                    print("@")
                else
                    print("_")
            }
            println()
        }
        println()
    }
}

// stringToGeneration :: String -> Generation
fun stringToGeneration(string: String): Generation =
        if (string.isBlank()) emptyList()
        else {
            string.split(delimiters = *arrayOf("\n"))
                    .mapIndexed { y, s ->
                        s.mapIndexed { x, c -> Pair(c, Position(x, y)) }
                    }
                    .flatten()
                    .filter { pair -> pair.first == '@' }
                    .map { pair -> pair.second }
        }

// impure but immutable, iteration hidden behind tail recursion
// runGame :: Generation -> Int -> IO (String)
tailrec fun runGame(generation: Generation, iterations: Int): Unit =
        when (iterations) {
            0 -> println("Game finished!")
            else -> {
                println("Iteration: $iterations")
                printGeneration(generation)
                Thread.sleep(500)
                runGame(getNextGeneration(generation), iterations - 1)
            }
        }

fun main(args: Array<String>) {
    val initial =
            """
            _@_
            @@@
            _@_
            """.trimIndent()

    runGame(generation = stringToGeneration(initial),
            iterations = 100)
}