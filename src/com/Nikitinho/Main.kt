package com.Nikitinho

import java.util.*
import kotlin.collections.ArrayList

// Trees' setup
const val TRESS_NUMBER = 100
const val DIFFERENT_TREES = 6

const val MAX_NUTS_AND_CONES_NUMBER = 10
const val MAX_MAPLE_LEAVES_NUMBER = 20
const val MAX_CAVITIES_NUMBER = 10
const val MAX_WORMS_NUMBER = 50
const val MAX_HOLES_NUMBER = 30
const val MAX_TUBERS_NUMBER = 10

// Animals' setup
const val ANIMALS_NUMBER = 200
const val DIFFERENT_ANIMALS = 5
const val MAX_HUNGER = 30
const val MAX_AMOUNT_OF_FOOD = 5
const val MAX_FOOD_HP_REGEN = 5

const val PROGRAM_CYCLES = 10

fun main(args: Array<String>) {
    println("========Trees generation========")
    println("--------------------------------")
    val trees = generateTrees(TRESS_NUMBER)
    println("--------------------------------")
    Thread.sleep(5000)

    println("=======Beasts generation========")
    println("--------------------------------")
    val beasts = generateAnimals(ANIMALS_NUMBER)
    println("--------------------------------")
    Thread.sleep(5000)

    var cycles = 0
    while (cycles < PROGRAM_CYCLES) {

        println("=============Hunger=============")
        println("--------------------------------")
        animalsDying(beasts)
        beasts.removeAll { it.hp() <= 0 }
        println("--------------------------------")
        Thread.sleep(5000)

        println("==============Meal==============")
        println("--------------------------------")
        animalsMeal(beasts, trees)
        println("--------------------------------")
        Thread.sleep(5000)

        println("=========Regeneration===========")
        println("--------------------------------")
        regeneration(trees)
        println("--------------------------------")
        Thread.sleep(5000)

        println("======Finding new friends=======")
        println("--------------------------------")
        newFriens(beasts, trees)
        println("--------------------------------")
        Thread.sleep(5000)

        cycles++
    }
}

val random = Random()

// Returns random tree
fun randomTree(): TreeStructure {
    return when (random.nextInt(DIFFERENT_TREES)) {
        0 -> Spruce()
        1 -> Pine()
        2 -> Oak()
        3 -> Birch()
        4 -> Maple()
        else -> Walnut()
    }
}

// Generating trees
fun generateTrees(treesCount: Int): ArrayList<TreeStructure> {
    val trees = ArrayList<TreeStructure>(treesCount)
    (1..treesCount).forEach {
        val tree = randomTree()
        println("""${tree.name} was created""")

        treeCustomization(tree)
        trees.add(tree)
    }
    println("--------------------------------")
    println("""$treesCount trees were successfully created""")

    return trees
}

// Returns random animal
fun randomAnimal(): Animals {
    return when (random.nextInt(DIFFERENT_ANIMALS)) {
        0 -> Squirrel()
        1 -> Chipmunk()
        2 -> Badger()
        3 -> Flying_Squirrel()
        else -> Woodpecker()
    }
}

// Generating animals
fun generateAnimals(animalsCount: Int): ArrayList<Animals> {
    val beasts = ArrayList<Animals>(animalsCount)
    (1..animalsCount).forEach {
        val beast = randomAnimal()
        println("""${beast.name} was added""")
        println("""${beast.hp()} hp""")
        beasts.add(beast)
    }

    println("--------------------------------")
    println("""$animalsCount animals were successfully created""")

    return beasts
}

// Hunger
fun animalsDying(beasts: ArrayList<Animals>){
    beasts.forEach{ it ->
        val hunger = random.nextInt(MAX_HUNGER + 1)
        it.hunger(hunger)
        println("""${it.name} - $hunger hp = ${it.hp()}""")
    }
}

fun treeCustomization (tree: TreeStructure) {
    if (tree.name === "Spruce" || tree.name === "Pine" || tree.name === "Walnut") {
        val nutsNConesNumber = random.nextInt(MAX_NUTS_AND_CONES_NUMBER + 1)
        val newFruits = ArrayList<TreeStructure.NutsAndCones>(0)
        (1..nutsNConesNumber).forEach {
            when (random.nextInt(2)) {
                0 -> newFruits.add(TreeStructure.NutsAndCones.COMMON)
                else -> newFruits.add(TreeStructure.NutsAndCones.FALLEN)
            }
        }
        tree.crown.addNutsAndCones(newFruits)
    } else if (tree.name === "Maple"){
        val newLeaves = random.nextInt(MAX_MAPLE_LEAVES_NUMBER + 1)
        tree.crown.leavesNumberChanged(newLeaves)
        println("""$newLeaves leaves were added to ${tree.name}""")
    }

    val addedCavities = random.nextInt(MAX_CAVITIES_NUMBER + 1)
    tree.trunk.cavitiesNumberChanged(addedCavities)
    println("""$addedCavities cavities were added to ${tree.name}""")

    val addedWorms = random.nextInt(MAX_WORMS_NUMBER + 1)
    tree.trunk.wormsNumberChanged(addedWorms)
    println("""$addedWorms worms were added to ${tree.name}""")

    val addedHoles = random.nextInt(MAX_HOLES_NUMBER + 1)
    tree.roots.holesNumberChanged(addedHoles)
    println("""$addedHoles holes were added to ${tree.name}""")

    val addedTubers = random.nextInt(MAX_TUBERS_NUMBER + 1)
    tree.roots.tubersNumberChanged(addedHoles)
    println("""$addedTubers tubers were added to ${tree.name}""")
}

// Adding extra resources
fun regeneration(trees: ArrayList<TreeStructure>) {
    val treesCount = trees.size
    (0 until treesCount).forEach {
        val tree = trees[it]
        treeCustomization(tree)
    }
}

// Meal
fun animalsMeal(beasts: ArrayList<Animals>, trees: ArrayList<TreeStructure>){
    beasts.forEach{ it ->
        val amountOfFood = neededFood(it, trees)
        val mealRegen = random.nextInt(MAX_FOOD_HP_REGEN+ 1) * amountOfFood
        it.hunger(-mealRegen)
        println("""${it.name} + $mealRegen hp = ${it.hp()}""")
    }
}

// Food needed for one particular animal
fun neededFood(beast: Animals, trees: ArrayList<TreeStructure>): Int {
    when (beast.name) {
        "Squirrel" -> {
            val tree = trees.find { it.crown.fruitsExist() }
            if (tree != null){
                val foodNeed = tree.crown.fruitsAvailable(random.nextInt(MAX_AMOUNT_OF_FOOD + 1))
                return tree.crown.removeNutsAndCones(TreeStructure.NutsAndCones.COMMON, foodNeed)
            }
            else
                return 0
        }
        "Chipmunk" -> {
            val tree = trees.find { it.crown.fruitsExist() }
            if (tree != null) {
                val foodNeed = tree.crown.fruitsAvailable(random.nextInt(MAX_AMOUNT_OF_FOOD + 1))
                return tree.crown.removeNutsAndCones(TreeStructure.NutsAndCones.FALLEN, foodNeed)
            }
            else
                return 0
        }
        "Badger" -> {
            val tree = trees.find { it.roots.tubersExist() }
            if (tree != null) {
                val foodNeed = tree.roots.tubersAvailable(random.nextInt(MAX_AMOUNT_OF_FOOD + 1))
                return tree.roots.tubersNumberChanged(-foodNeed)
            }
            else
                return 0
        }
        "Flying_Squirrel" -> {
            val tree = trees.find { it.crown.leavesExist() }
            if (tree != null) {
                val foodNeed = tree.crown.leavesAvailable(random.nextInt(MAX_AMOUNT_OF_FOOD + 1))
                return tree.crown.leavesNumberChanged(-foodNeed)
            }
            else
                return 0
        }
        else /*Woodpecker*/ -> {
            val tree = trees.find { it.trunk.wormsExist() }
            if (tree != null) {
                val foodNeed = tree.trunk.wormsAvailable(random.nextInt(MAX_AMOUNT_OF_FOOD + 1))
                return tree.trunk.wormsNumberChanged(-foodNeed)
            }
            else
                return 0
        }
    }
}


fun newFriens(beasts: ArrayList<Animals>, trees: ArrayList<TreeStructure>) {
    findPlaces(beasts, trees)
}

fun findPlaces(beasts: ArrayList<Animals>, trees: ArrayList<TreeStructure>) {
    beasts.forEach{
        val tree = trees[random.nextInt(trees.size)]
        val treePart = randomPart()
        it.place(tree, treePart)
        println("""${it.name} is on the ${tree.name} now! ($treePart)""")
    }
}

fun randomPart(): String {
    return when (random.nextInt(4)) {
        0 -> "Crown"
        1 -> "Trunk"
        else -> "Roots"
    }
}

interface Animals {
    val gender: Gender
    val name: String

    fun hunger(food: Int)
    fun hp():Int
    fun place(tree: TreeStructure?, place: String?)

    enum class Gender {
        MALE,
        FEMALE
    }
}

fun randomGender(): Animals.Gender {
    return when (random.nextInt(2)) {
        0 -> Animals.Gender.MALE
        else -> Animals.Gender.FEMALE
    }
}

/*Белочки питаются орешками и шишками*/
data class Squirrel(private var food: Int = 100,
                    override val gender: Animals.Gender = randomGender(),
                    private var tree: TreeStructure? = null,
                    private var place: String? = null,
                    override val name: String = "Squirrel"): Animals {
    override fun hunger(food: Int) {
        this.food -= food
        if (this.food < 0) {
            this.food = 0
            println("""${this.name} died""")
        }
    }
    override fun place(tree:TreeStructure?, place: String?) {
        this.tree = tree
        this.place = place
    }
    override fun hp() = food
}

/*Бурундучки едят орешки и шишки, но опавшие*/
data class Chipmunk(private var food: Int = 100,
                    override val gender: Animals.Gender = randomGender(),
                    private var tree: TreeStructure? = null,
                    private var place: String? = null,
                    override val name: String = "Chipmunk"): Animals {
    override fun hunger(food: Int) {
        this.food -= food
        if (this.food < 0) {
            this.food = 0
            println("""${this.name} died""")
        }
    }
    override fun place(tree:TreeStructure?, place: String?) {
        this.tree = tree
        this.place = place
    }
    override fun hp() = food
}

/*Барсуки едят корнеплоды, которые встречаются в некоторых корнях.*/
data class Badger(private var food: Int = 100,
                  override val gender: Animals.Gender = randomGender(),
                  private var tree: TreeStructure? = null,
                  private var place: String? = null,
                  override val name: String = "Badger"): Animals {
    override fun hunger(food: Int) {
        this.food -= food
        if (this.food < 0) {
            this.food = 0
            println("""${this.name} died""")
        }
    }
    override fun place(tree:TreeStructure?, place: String?) {
        this.tree = tree
        this.place = place
    }
    override fun hp() = food
}

/*Летяги едят кленовые листья (есть в кронах кленов)*/
data class Flying_Squirrel(private var food: Int = 100,
                           override val gender: Animals.Gender = randomGender(),
                           private var tree: TreeStructure? = null,
                           private var place: String? = null,
                           override val name: String = "Flying_Squirrel"): Animals {
    override fun hunger(food: Int) {
        this.food -= food
        if (this.food < 0) {
            this.food = 0
            println("""${this.name} died""")
        }
    }
    override fun place(tree:TreeStructure?, place: String?) {
        this.tree = tree
        this.place = place
    }
    override fun hp() = food
}

/*Дятлы едят червячков, которые живут в стволах.*/
data class Woodpecker(private var food: Int = 100,
                      override val gender: Animals.Gender = randomGender(),
                      private var tree: TreeStructure? = null,
                      private var place: String? = null,
                      override val name: String = "Woodpecker"): Animals {
    override fun hunger(food: Int) {
        this.food -= food
        if (this.food < 0) {
            this.food = 0
            println("""${this.name} died""")
        }
    }
    override fun place(tree:TreeStructure?, place: String?) {
        this.tree = tree
        this.place = place
    }
    override fun hp() = food
}


interface TreeStructure {
    val crown: Crown
    val trunk: Trunk
    val roots: Roots

    val name: String

    data class Crown (private val nutsNCones: ArrayList<NutsAndCones> = ArrayList(0),
                      private var mapleLeaves: Int = 0) {
        // Add list of nuts and cones
        fun addNutsAndCones(added: ArrayList<NutsAndCones>) {
            added.forEach { element -> nutsNCones.add(element) }
        }

        // Remove nuts and cones of a particular type while it is possible
        fun removeNutsAndCones(type: NutsAndCones, count: Int): Int {
            var possible = 0
            for (i in 1..count) {
                if (nutsNCones.indexOfLast { it.ordinal == type.ordinal } >= 0) {
                    nutsNCones.removeAt(nutsNCones.indexOfLast { it.ordinal == type.ordinal })
                    possible++
                }
                else
                    break
            }
            return possible
        }

        // Add/remove maple leaves (for maples)
        fun leavesNumberChanged(leaves: Int): Int {
            val before = mapleLeaves
            mapleLeaves += leaves

            if (mapleLeaves < 0)
                mapleLeaves = 0

            return before - mapleLeaves
        }

        fun fruitsExist() = nutsNCones.isNotEmpty()
        fun leavesExist() = mapleLeaves > 0

        fun fruitsAvailable(needed: Int) = minOf(nutsNCones.count(), needed)
        fun leavesAvailable(needed: Int) = minOf(mapleLeaves, needed)
    }

    data class Trunk /*Ствол*/ (private var cavities: Int = 0, private var worms: Int = 0) {
        // Add/remove cavities
        fun cavitiesNumberChanged(cavities: Int) {
            this.cavities += cavities

            if (this.cavities < 0)
                this.cavities = 0
        }

        // Add/remove worms
        fun wormsNumberChanged(worms: Int): Int {
            val before = this.worms
            this.worms += worms

            if (this.worms < 0)
                this.worms = 0
            return before - this.worms
        }

        fun cavitiesExist() = cavities > 0
        fun wormsExist() = worms > 0

        fun wormsAvailable(needed: Int) = minOf(worms, needed)
    }

    data class Roots (private var holes: Int = 0, private var tubers /*корнеплоды*/: Int = 0) {
        // Add/remove holes
        fun holesNumberChanged(holes: Int) {
            this.holes += holes

            if (this.holes < 0)
                this.holes = 0
        }

        // Add/remove tubers
        fun tubersNumberChanged(tubers: Int): Int {
            val before = this.tubers
            this.tubers += tubers

            if (this.tubers < 0)
                this.tubers = 0
            return before - this.tubers
        }

        fun holesExist() = holes > 0
        fun tubersExist() = tubers > 0

        fun tubersAvailable(needed: Int) = minOf(tubers, needed)
    }

    enum class NutsAndCones /*Орешки и шишки*/ {
        FALLEN,
        COMMON
    }
}


data class Spruce/*Ель с шишками*/(override val crown: TreeStructure.Crown = TreeStructure.Crown(),
                                   override val trunk: TreeStructure.Trunk = TreeStructure.Trunk(),
                                   override val roots: TreeStructure.Roots = TreeStructure.Roots(),
                                   override val name: String = "Spruce"): TreeStructure

data class Pine/*Сосна с шишками и орехами*/(override val crown: TreeStructure.Crown = TreeStructure.Crown(),
                                             override val trunk: TreeStructure.Trunk = TreeStructure.Trunk(),
                                             override val roots: TreeStructure.Roots = TreeStructure.Roots(),
                                             override val name: String = "Pine"): TreeStructure

data class Oak/*Дуб*/(override val crown: TreeStructure.Crown = TreeStructure.Crown(),
                      override val trunk: TreeStructure.Trunk = TreeStructure.Trunk(),
                      override val roots: TreeStructure.Roots = TreeStructure.Roots(),
                      override val name: String = "Oak"): TreeStructure

data class Birch/*Береза*/(override val crown: TreeStructure.Crown = TreeStructure.Crown(),
                           override val trunk: TreeStructure.Trunk = TreeStructure.Trunk(),
                           override val roots: TreeStructure.Roots = TreeStructure.Roots(),
                           override val name: String = "Birch"): TreeStructure

data class Maple/*Клен с кленовыми листьями*/(override val crown: TreeStructure.Crown = TreeStructure.Crown(),
                                              override val trunk: TreeStructure.Trunk = TreeStructure.Trunk(),
                                              override val roots: TreeStructure.Roots = TreeStructure.Roots(),
                                              override val name: String = "Maple"): TreeStructure

data class Walnut/*Орех с орехами*/(override val crown: TreeStructure.Crown = TreeStructure.Crown(),
                                    override val trunk: TreeStructure.Trunk = TreeStructure.Trunk(),
                                    override val roots: TreeStructure.Roots = TreeStructure.Roots(),
                                    override val name: String = "Walnut"): TreeStructure