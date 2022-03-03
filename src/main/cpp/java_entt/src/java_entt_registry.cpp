#include "java_entt_registry.h"
#include <cstdio>
#include <entt/entity/fwd.hpp>
#include <entt/entity/registry.hpp>
#include <entt/entt.hpp>
#include <iostream>
#include <memory>
#include <new>

namespace JavaEntt {
entt::registry reg;

void update(entt::registry &registry) {
  auto view = registry.view<position, velocity>();

  // use a range-for
  for (auto [entity, pos, vel] : view.each()) {
    reg.replace<position>(entity, pos.x + 0.5f * (float)entity, pos.y + 0.01f);
  }
}

void printAllEntities(entt::registry &registry) {
  auto view = registry.view<position, velocity>();

  // use a range-for
  for (auto [entity, pos, vel] : view.each()) {
    printf("(entity: %u) x: %f y: %f\n", entity, pos.x, pos.y);
  }
  printf("Capacity: %lu\n", registry.capacity());
}

} // namespace JavaEntt

// Export functions

JNIEXPORT void JNICALL init() {}

JNIEXPORT void JNICALL registerPosition(position &pos) {
  const auto entity = JavaEntt::reg.create();
  JavaEntt::reg.emplace<position>(entity, pos.x, pos.y);
  JavaEntt::reg.emplace<velocity>(
      entity, pos.x, pos.y);
}

JNIEXPORT void JNICALL registerPositions(position pos[], int length) {
  for (int i = 0; i < length; i++) {
    const auto entity = JavaEntt::reg.create();
    JavaEntt::reg.emplace<position>(entity, pos[i].x, pos[i].y);
    JavaEntt::reg.emplace<velocity>(
        entity, pos[i].x, pos[i].y);
  }
}

JNIEXPORT int JNICALL update() {
  JavaEntt::update(JavaEntt::reg);
  return 1;
}

JNIEXPORT void JNICALL printAllEntities() {
  JavaEntt::printAllEntities(JavaEntt::reg);
}

JNIEXPORT int JNICALL getCapacity(void) {
  return JavaEntt::reg.capacity();
}

JNIEXPORT position* JNICALL getPositions(void) {
  position* arr = new position[JavaEntt::reg.capacity()];
  auto view = JavaEntt::reg.view<position, velocity>();
  int i = 0;
  for (auto [entity, pos, vel] : view.each()) {
    arr[i++] = position{ pos.x, pos.y };
  }
  
  return arr;
}
