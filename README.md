Замеряется суммарное время первой отрисовки в мс onDraw у 10-ти ImageView размером 24dp. Размер ресурсов 24dp

Время работы:
Samsung Ace2(4.1.2)
 1) Черные png - 0.5
 2) Черные png + у каждой свой тинт - 0.6 
 3) Черные png + у каждой ColorStateList тинт ЧЕРЕЗ XML  - ВОЗМОЖНОСТИ НЕТ
 4) Черные png + у каждой ColorStateList тинт ЧЕРЕЗ КОД  - 0.6
 5) Черные VectorDrawableCompat - 14
 6) Черные VectorDrawableCompat + у каждой свой тинт ЧЕРЕЗ КОД - 20
 7) Черные VectorDrawableCompat + у каждой ColorStateList тинт ЧЕРЕЗ КОД  - 20

Sony xperia xa1(7.0)
 1) Черные png - 0.4
 2) Черные png + у каждой свой тинт - 0.4 
 3) Черные png + у каждой ColorStateList тинт ЧЕРЕЗ XML  - 0.4
 4) Черные png + у каждой ColorStateList тинт ЧЕРЕЗ КОД  - 0.4
 5) Черные VectorDrawable - 0.4
 6) Черные VectorDrawable + у каждой свой тинт ЧЕРЕЗ КОД - 0.4 
 7) Черные VectorDrawable + у каждой ColorStateList тинт ЧЕРЕЗ КОД  - 0.4
