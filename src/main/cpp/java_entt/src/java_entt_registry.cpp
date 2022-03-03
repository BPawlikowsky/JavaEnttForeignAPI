#include "java_entt_registry.h"
#include <cstdio>
#include <entt/entity/fwd.hpp>
#include <entt/entity/registry.hpp>
#include <entt/entt.hpp>
#include <iostream>
#include <memory>


namespace JavaEntt {
    entt::registry reg;

void update(entt::registry &registry) {
  auto view = registry.view<const position, velocity>();

  // use a range-for
  for (auto [entity, pos, vel] : view.each()) {
    printf("Pos: %f %f %u \n", pos.x, pos.y, entity);
  }
  printf("Capacity: %lu", registry.capacity());
}
  
} // namespace JavaEntt

// Export functions

JNIEXPORT void JNICALL init() {
}

JNIEXPORT position JNICALL create(position& pos) {
    float posx, posy;
    posx = pos.x;
    posy = pos.y;
    const auto entity = JavaEntt::reg.create();
    JavaEntt::reg.emplace<position>(entity, posx + 0.1f * (JavaEntt::reg.capacity() * 0.5f), posy);
    JavaEntt::reg.emplace<velocity>(entity, posx, posy + 0.01f * (JavaEntt::reg.capacity() * 0.5f));
    
    return position{posx, posy};
}

JNIEXPORT int JNICALL update() { 
  JavaEntt::update(JavaEntt::reg); 
  return 1;
}
