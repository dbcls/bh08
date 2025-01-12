require 'bio'

input = File.open('aj224122.embl').read
embl_object_1 = Bio::EMBL.new(input)
bioseq_1 = embl_object_1.to_biosequence
puts bioseq_1.output(:embl)

