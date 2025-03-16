package com.softuni.project.mapper;

public interface Mapper<S, T> {
    T map(S source);
}
