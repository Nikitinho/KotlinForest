import io.kotlintest.specs.FunSpec

class MyTest : FunSpec() {
    init {
        test("Check if the number of generated trees is correct") {
            val count = 100
            generateTrees(count).count() shouldBe count
        }
        test("Check if the number of generated beasts is correct") {
            val count = 20
            generateAnimals(count).count() shouldBe count
        }

        // trying to test trees
        // THESE SETTINGS CAN BE CHANGED MANUALLY

        // crown specifications
        val nutsNCones = arrayListOf<TreeStructure.NutsAndCones>()
        val numberOfCommon =  10 // can be changed
        val numberOfFallen = 5 // can be changed
        for (i in 0 until  numberOfCommon)
            nutsNCones.add(TreeStructure.NutsAndCones.COMMON)
        for (i in 0 until  numberOfFallen)
            nutsNCones.add(TreeStructure.NutsAndCones.FALLEN)
        val mapleLeaves = 6

        // trunk specification
        val cavities = 4 // can be changed
        val worms = 6 // can be changed

        // roots specification
        val holes = 8 // can be changed
        val tubers = 5 // can be changed

        val spruce = Spruce(TreeStructure.Crown(nutsNCones, mapleLeaves),
                            TreeStructure.Trunk(cavities, worms),
                            TreeStructure.Roots(holes, tubers))

        test("Check if the number of nuts and cones is correct") {
            spruce.crown.fruitsAvailable(numberOfFallen + numberOfCommon) shouldBe
                    numberOfCommon + numberOfFallen
            spruce.crown.fruitsAvailable(1) shouldBe (when (numberOfCommon + numberOfFallen >= 1)
            {
                true -> 1
                false -> 0
            })
            spruce.crown.fruitsAvailable(numberOfFallen + numberOfCommon + 100) shouldBe
                    numberOfCommon + numberOfFallen
        }

        test("Check if the number of maple leaves is correct") {
            spruce.crown.leavesAvailable(mapleLeaves) shouldBe mapleLeaves
            spruce.crown.leavesAvailable(1) shouldBe (when (mapleLeaves >= 1)
            {
                true -> 1
                false -> 0
            })
            spruce.crown.leavesAvailable(mapleLeaves + 100) shouldBe mapleLeaves
        }

        test("Check if cavities exist") {
            spruce.trunk.cavitiesExist() shouldEqual (cavities > 0)
        }

        test("Check if worms exist") {
            spruce.trunk.wormsExist() shouldEqual (worms > 0)
        }

        test("Check if holes exist") {
            spruce.roots.holesExist() shouldEqual (holes > 0)
        }

        test("Check if tubers exist") {
            spruce.roots.tubersExist() shouldEqual (tubers > 0)
        }

    }
}
