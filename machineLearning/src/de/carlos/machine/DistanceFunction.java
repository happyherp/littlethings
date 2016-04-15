package de.carlos.machine;

public interface DistanceFunction<T> {

	double calculateDistance(T a, T b);
}
