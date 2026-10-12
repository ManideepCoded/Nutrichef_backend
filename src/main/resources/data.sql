-- Initial Seed Recipes for NutriChef
INSERT INTO recipes (id, title, description, instructions, prep_time_minutes, cook_time_minutes, servings, difficulty, cuisine_type, image_url, calories_per_serving, protein_grams, carbs_grams, fat_grams, created_at)
VALUES
(1, 'Mediterranean Quinoa Salad Bowl', 'Fresh, protein-packed quinoa salad with diced cucumbers, cherry tomatoes, kalamata olives, and feta cheese drizzled in lemon-oregano vinaigrette.', '1. Cook quinoa according to package instructions and let cool.\n2. In a large bowl, combine diced cucumber, halved cherry tomatoes, sliced red onion, and kalamata olives.\n3. Whisk together extra virgin olive oil, fresh lemon juice, dried oregano, salt, and black pepper.\n4. Fluff quinoa and fold with chopped veggies and crumbled feta cheese.\n5. Drizzle dressing and serve chilled.', 15, 15, 2, 'EASY', 'Mediterranean', 'https://images.unsplash.com/photo-1540420773420-3366772f4999?auto=format&fit=crop&w=800&q=80', 380, 14.0, 48.0, 16.0, NOW()),

(2, 'Garlic Herb Chicken with Roasted Broccoli', 'Juicy seared chicken breast infused with rosemary, thyme, and roasted garlic alongside crispy charred broccoli florets.', '1. Preheat oven to 400°F (200°C).\n2. Toss broccoli florets with 1 tbsp olive oil, salt, and minced garlic; spread on baking sheet.\n3. Season chicken breast with salt, pepper, garlic powder, and fresh thyme.\n4. Sear chicken in an oven-safe skillet with olive oil for 3-4 minutes per side.\n5. Transfer skillet and baking sheet to oven and roast for 12-15 minutes until chicken hits 165°F (74°C).\n6. Rest chicken 5 minutes, slice, and serve with broccoli.', 15, 20, 2, 'MEDIUM', 'American', 'https://images.unsplash.com/photo-1532550907401-a500c9a57435?auto=format&fit=crop&w=800&q=80', 440, 46.0, 12.0, 22.0, NOW()),

(3, 'Spinach & Tomato Protein Omelette', 'Fluffy egg omelette folded with baby spinach, ripe tomatoes, and goat cheese. Perfect low-carb, nutrient-dense breakfast.', '1. In a small bowl, whisk eggs with milk, a pinch of salt, and black pepper.\n2. Heat olive oil or butter in a non-stick skillet over medium heat.\n3. Add baby spinach and halved cherry tomatoes; sauté for 1-2 minutes until wilted.\n4. Pour eggs evenly over vegetables. Cook gently, lifting edges to let runny egg flow underneath.\n5. Sprinkle crumbled goat cheese or feta over half the omelette.\n6. Fold in half, slide onto plate, and garnish with fresh parsley.', 5, 8, 1, 'EASY', 'Healthy', 'https://images.unsplash.com/photo-1510693206972-df098062cb71?auto=format&fit=crop&w=800&q=80', 310, 22.0, 6.0, 21.0, NOW()),

(4, 'Creamy Tuscan Garlic Salmon', 'Pan-seared tender salmon fillets nestled in a rich garlic cream sauce with sun-dried tomatoes and baby spinach.', '1. Season salmon fillets with sea salt and cracked black pepper.\n2. Sear salmon in hot skillet with olive oil for 5 minutes per side until golden; set aside.\n3. In same skillet, melt butter and sauté minced garlic for 1 minute.\n4. Add vegetable or chicken broth, heavy cream, and sliced sun-dried tomatoes; simmer 3 minutes.\n5. Fold in spinach until wilted. Return salmon to skillet, spoon sauce over fillets, and simmer 2 minutes.', 10, 15, 2, 'MEDIUM', 'Italian', 'https://images.unsplash.com/photo-1467003909585-2f8a72700288?auto=format&fit=crop&w=800&q=80', 520, 38.0, 10.0, 36.0, NOW()),

(5, 'Hearty Lentil & Sweet Potato Stew', 'Warming, fiber-dense vegan stew featuring brown lentils, cubed sweet potatoes, carrots, turmeric, and cumin simmered in vegetable broth.', '1. Heat olive oil in a Dutch oven or large pot over medium heat.\n2. Sauté chopped onion, minced garlic, and sliced celery until fragrant.\n3. Add cubed sweet potatoes, brown lentils, cumin, ground turmeric, and smoked paprika.\n4. Pour in vegetable broth and diced tomatoes. Bring to a boil, then reduce heat to low.\n5. Simmer covered for 30 minutes until sweet potatoes and lentils are fork-tender.\n6. Season with salt, pepper, and fresh lemon juice before ladling into bowls.', 15, 35, 4, 'EASY', 'Plant-Based', 'https://images.unsplash.com/photo-1547592166-23ac45744acd?auto=format&fit=crop&w=800&q=80', 360, 16.0, 62.0, 6.0, NOW())
ON DUPLICATE KEY UPDATE title=VALUES(title);

-- Recipe Ingredients
INSERT INTO recipe_ingredients (recipe_id, ingredient_name, normalized_name, quantity, unit, is_optional) VALUES
-- Recipe 1: Mediterranean Quinoa Salad
(1, 'Quinoa', 'quinoa', 1.0, 'cup', false),
(1, 'Cucumber', 'cucumber', 1.0, 'pcs', false),
(1, 'Cherry Tomatoes', 'tomato', 150.0, 'g', false),
(1, 'Feta Cheese', 'feta', 75.0, 'g', false),
(1, 'Olive Oil', 'olive oil', 2.0, 'tbsp', false),
(1, 'Lemon Juice', 'lemon', 1.0, 'pcs', false),

-- Recipe 2: Garlic Herb Chicken with Roasted Broccoli
(2, 'Chicken Breast', 'chicken', 400.0, 'g', false),
(2, 'Broccoli', 'broccoli', 300.0, 'g', false),
(2, 'Garlic', 'garlic', 4.0, 'cloves', false),
(2, 'Olive Oil', 'olive oil', 2.0, 'tbsp', false),
(2, 'Rosemary', 'rosemary', 1.0, 'tsp', true),

-- Recipe 3: Spinach & Tomato Protein Omelette
(3, 'Eggs', 'egg', 3.0, 'pcs', false),
(3, 'Baby Spinach', 'spinach', 50.0, 'g', false),
(3, 'Cherry Tomatoes', 'tomato', 60.0, 'g', false),
(3, 'Olive Oil', 'olive oil', 1.0, 'tbsp', false),
(3, 'Milk', 'milk', 2.0, 'tbsp', true),

-- Recipe 4: Creamy Tuscan Garlic Salmon
(4, 'Salmon Fillet', 'salmon', 350.0, 'g', false),
(4, 'Baby Spinach', 'spinach', 100.0, 'g', false),
(4, 'Garlic', 'garlic', 3.0, 'cloves', false),
(4, 'Heavy Cream', 'cream', 120.0, 'ml', false),
(4, 'Tomatoes', 'tomato', 80.0, 'g', false),

-- Recipe 5: Hearty Lentil Stew
(5, 'Brown Lentils', 'lentils', 200.0, 'g', false),
(5, 'Sweet Potato', 'sweet potato', 2.0, 'pcs', false),
(5, 'Carrots', 'carrot', 2.0, 'pcs', false),
(5, 'Onion', 'onion', 1.0, 'pcs', false),
(5, 'Garlic', 'garlic', 3.0, 'cloves', false);
