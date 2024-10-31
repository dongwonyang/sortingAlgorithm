package com.example.sortingalgorithm

import kotlin.random.Random

fun main() {
    val keySorting = KeySorting()

    keySorting.printSortingAlgorithm()
}


class KeySorting() {
    val intRange: IntRange = 1..10000

    val randomArray32 = generateRandomArray(32, intRange)
    val randomArray1024 = generateRandomArray(1024, intRange)

    private fun generateRandomArray(size: Int, range: IntRange): IntArray {
        return IntArray(size) { Random.nextInt(range.first, range.last + 1) }
    }


    private fun insertionSort(array: IntArray): Triple<IntArray, Int, String> {
        val array = array
        val startTime = System.nanoTime()
        var comparisonCnt = 0
        for (current in 1 until array.size) {
            val temp = array[current]
            var comparison = current - 1

            while (comparison >= 0) {
                comparisonCnt++  // 비교할 때마다 count 증가
                if (array[comparison] > temp) {
                    array[comparison + 1] = array[comparison]
                    comparison--
                } else {
                    break
                }
            }
            array[comparison + 1] = temp
        }

        val durationInNanoseconds = System.nanoTime() - startTime
        val duration = durationInNanoseconds / 1_000_000_000.0
        return Triple(array, comparisonCnt, String.format("%f초", duration))
    }


    private fun heapSort(array: IntArray): Triple<IntArray, Int, String> {
        val array = array
        val startTime = System.nanoTime()
        var comparisonCnt = 0

        fun maxHeapify(size: Int, rootIndex: Int) {
            var largest = rootIndex
            val leftChild = 2 * rootIndex + 1
            val rightChild = 2 * rootIndex + 2

            if (leftChild < size) {
                comparisonCnt++  // 부모와 왼쪽 자식 비교
                if (array[leftChild] > array[largest]) {
                    largest = leftChild
                }
            }

            if (rightChild < size) {
                comparisonCnt++  // 부모와 오른쪽 자식 비교
                if (array[rightChild] > array[largest]) {
                    largest = rightChild
                }
            }

            if (largest != rootIndex) {
                val temp = array[rootIndex]
                array[rootIndex] = array[largest]
                array[largest] = temp
                maxHeapify(size, largest)
            }
        }

        fun buildMaxHeap() {
            for (i in array.size / 2 - 1 downTo 0) {
                maxHeapify(array.size, i)
            }
        }

        buildMaxHeap()
        for (i in array.size - 1 downTo 1) {
            val temp = array[0]
            array[0] = array[i]
            array[i] = temp

            maxHeapify(i, 0)
        }

        val durationInNanoseconds = System.nanoTime() - startTime // 나노초로 걸린 시간 측정
        val duration = durationInNanoseconds / 1_000_000_000.0 // 나노초를 Duration으로 변환

        return Triple(array, comparisonCnt, String.format("%f초", duration))
    }

    private fun mergeSort(array: IntArray): Triple<IntArray, Int, String> {
        val array = array
        val startTime = System.nanoTime()
        var comparisonCnt = 0

        fun merge(left: IntArray, right: IntArray): IntArray {
            val merged = IntArray(left.size + right.size)
            var leftIndex = 0
            var rightIndex = 0
            var mergedIndex = 0

            while (leftIndex < left.size && rightIndex < right.size) {
                comparisonCnt++  // 비교할 때마다 카운트 증가
                if (left[leftIndex] <= right[rightIndex]) {
                    merged[mergedIndex++] = left[leftIndex++]
                } else {
                    merged[mergedIndex++] = right[rightIndex++]
                }
            }

            while (leftIndex < left.size) {
                merged[mergedIndex++] = left[leftIndex++]
            }

            while (rightIndex < right.size) {
                merged[mergedIndex++] = right[rightIndex++]
            }

            return merged
        }

        if (array.size <= 1) return Triple(array, comparisonCnt, "")

        val mid = array.size / 2

        val (sortedFront, leftCount) = mergeSort(array.sliceArray(0 until mid))
        val (sortedBack, rightCount) = mergeSort(array.sliceArray(mid until array.size))

        comparisonCnt += leftCount + rightCount  // 총 비교 횟수 합산
        val mergedArray = merge(sortedFront, sortedBack)

        val durationInNanoseconds = System.nanoTime() - startTime // 나노초로 걸린 시간 측정
        val duration = durationInNanoseconds / 1_000_000_000.0 // 나노초를 Duration으로 변환

        return Triple(mergedArray, comparisonCnt, String.format("%f초", duration))
    }


    fun quickSort(array: IntArray, pivotType: PivotType): Triple<IntArray, Int, String> {
        val array = array
        val startTime = System.nanoTime()
        var comparisonCnt = 0

        fun getPivotIndex(start: Int, end: Int, pivotType: PivotType): Int {
            return when (pivotType) {
                PivotType.VERSION1 -> start
                PivotType.VERSION2 -> Random.nextInt(start, end + 1)
                PivotType.VERSION3 -> {
                    val mid = (start + end) / 2
                    if (array[start] < array[mid]) {
                        if (array[mid] < array[end]) mid else end
                    } else {
                        if (array[start] < array[end]) start else end
                    }
                }

                PivotType.VERSION4 -> (start + end) / 2
            }
        }

        fun swap(i: Int, j: Int) {
            val temp = array[i]
            array[i] = array[j]
            array[j] = temp
        }

        fun quickSortRecursive(start: Int, end: Int) {
            if (start >= end) return

            val pivotIndex = getPivotIndex(start, end, pivotType)
            val pivot = array[pivotIndex]

            swap(pivotIndex, end)

            var partitionIndex = start
            for (i in start until end) {
                comparisonCnt++ // 비교할 때마다 카운트 증가
                if (array[i] < pivot) {
                    swap(i, partitionIndex)
                    partitionIndex++
                }
            }
            swap(partitionIndex, end)

            quickSortRecursive(start, partitionIndex - 1)
            quickSortRecursive(partitionIndex + 1, end)
        }

        quickSortRecursive(0, array.size - 1)

        val durationInNanoseconds = System.nanoTime() - startTime // 나노초로 걸린 시간 측정
        val duration = durationInNanoseconds / 1_000_000_000.0 // 나노초를 Duration으로 변환

        return Triple(array, comparisonCnt, String.format("%f초", duration))
    }

    enum class PivotType {
        VERSION1, //first or last element as a pivot
        VERSION2, //random number as a pivot
        VERSION3, //median of three partitioning as a pivot
        VERSION4 //your strategy(wisely but efficiently)
    }

    fun printSortingAlgorithm() {
        println("array32: ${randomArray32.joinToString(", ")}")
        println("array1024: ${randomArray1024.joinToString(", ")}\n")

        val (insertion32Array, insertion32Cnt, insertion32Time) = insertionSort(randomArray32)
        val (insertion1024Array, insertion1024Cnt, insertion1024Time) = insertionSort(
            randomArray1024
        )

        val (heap32Array, heap32Cnt, heap32Time) = heapSort(randomArray32)
        val (heap1024Array, heap1024Cnt, heap1024Time) = heapSort(randomArray1024)

        val (merge32Array, merge32Cnt, merge32Time) = mergeSort(randomArray32)
        val (merge1024Array, merge1024Cnt, merge1024Time) = mergeSort(randomArray1024)

        val quick32List = listOf(
            quickSort(randomArray32, PivotType.VERSION1),
            quickSort(randomArray32, PivotType.VERSION2),
            quickSort(randomArray32, PivotType.VERSION3),
            quickSort(randomArray32, PivotType.VERSION4)
        )

        val quick1024List = listOf(
            quickSort(randomArray1024, PivotType.VERSION1),
            quickSort(randomArray1024, PivotType.VERSION2),
            quickSort(randomArray1024, PivotType.VERSION3),
            quickSort(randomArray1024, PivotType.VERSION4)
        )

        println("Insertion Sort")
        println("sorting count 32: $insertion32Cnt , sorting time 32: $insertion32Time")
        println("sorting count 1024: $insertion1024Cnt , sorting time 1024: $insertion1024Time\n")

        println("Heap Sort")
        println("sorting count 32: $heap32Cnt , sorting time 32: $heap32Time")
        println("sorting count 1024: $heap1024Cnt , sorting time 1024: $heap1024Time\n")

        println("Merge Sort")
        println("sorting count 32: $merge32Cnt , sorting time: $merge32Time")
        println("sorting count 1024: $merge1024Cnt , sorting time 1024: $merge1024Time\n")

        println("Quick Sort ")
        for (quickResult in quick32List.withIndex()) {
            println("Version ${quickResult.index + 1}")
            println("sorting count 32: ${quickResult.value.second} , sorting time 32: ${quickResult.value.third}")
        }

        println()

        for (quickResult in quick1024List.withIndex()) {
            println("Version ${quickResult.index + 1}")
            println("sorting count 1024: ${quickResult.value.second} , sorting time 1024: ${quickResult.value.third}")
        }

        println("insertion: ${insertion32Array.joinToString(", ")}")
        println("heap: ${heap32Array.joinToString(", ")}")
        println("merge: ${merge32Array.joinToString(", ")}")
        println("quick: ${quick32List[1].first.joinToString(", ")}")

        println("insertion: ${insertion1024Array.joinToString(", ")}")
        println("heap: ${heap1024Array.joinToString(", ")}")
        println("merge: ${merge1024Array.joinToString(", ")}")
        println("quick: ${quick1024List[1].first.joinToString(", ")}")
    }
}