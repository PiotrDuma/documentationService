package com.github.PiotrDuma.documentationService.service.dao;

import java.util.List;
import java.util.Optional;


public interface DaoInterface<T> {

   T save(T t);
   Optional<T> get(Long id);
   List<T> getAll();
   T update(Long id, T t);
   void delete(Long id);
   boolean isPreset(T t);
   boolean isPreset(Long id);
}
