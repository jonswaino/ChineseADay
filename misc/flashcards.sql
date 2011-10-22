-- to do list
--  * Show pinyin with accents not numbers (Use pinyinforj?)
--  * Investigate why some chars haven't imported ok - RE-DONE

--  * Get learnt card to add into db
--  * Get word list to show all except learnt
--  * Get initial menu to be db driven (in decks)
--  * show definitions in better list
--  * Show well-done card when finished
--  * Make buttons better images/

--  * Create nice icon for Chinese Character Flashcards
--  * Create nice background for character


-- future versions
--   1. show all learnt words
--   2. allow user to select shuffle/order

-- get all characters read
select * from characters
where read = 1
order by id desc


-- get all characters in 1st deck
select * from characters
where id > 0 and id <= 100

select * from characters
where id > 100 and id <= 200

-- set menu to select 
--  - 100 characters
--  - 100-200 characters
--  - 200-300 characters
-- 
-- menu id (selects the id)
set @deck = 1
set @range = 100

set @min = ((@deck-1) * @range)+1
set @max = (@deck) * @range

select * from characters
where id > @min and id <= @max

select minseries || ' to ' || maxseries from decks


select * from characters as c 
inner join decks as d on c.id between d.minseries and d.maxseries
where d.minseries = 100

select * from decks

select * from decks as top 
inner join decks as subdeck on subdeck.minseries >= top.minseries and subdeck.minseries <= top.maxseries
where top.topmenu = 'true' and top.minseries = 1

select * from decks as top
where top.topmenu = 'true'