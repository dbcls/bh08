#!/usr/bin/env perl
#
# bioperl_convert.pl  v. 0.2
# 28.2.2008
# Heikki Lehvaslaiho, heikki a bioperl.org
#
# Run this in the same directory with the test files
#  and use grep and diff to analyse differences between
#  original and processed files
#
# e.g.
# > bioperl_convert.pl aj224122
#   # will work on embl, genbank and swissprot files..
# > diff aj224122.swiss aj224122-bioperl.swiss
# > bioperl_convert.pl aj224122-bioperl
# > diff aj224122-bioperl.swiss aj224122-bioperl-bioperl.swiss
#

use strict;
use warnings;
use Data::Dumper;

sub conv ($$$);

my $base = shift;
die "Usage: convert.pl basename\n" unless $base;

my @formats = qw( fasta embl genbank swiss);

for my $f (@formats) {

    my $ext = $f;
    $ext = 'fa' if $f eq 'fasta';
    $ext = 'gb' if $f eq 'genbank';


    #`seqconvert.pl --from $f --to $f < $base.$ext > $base-bioperl.$ext`;
    conv($base, $ext, $f);
}


sub conv ($$$) {

    my ($base, $ext, $format) = @_;
    return unless -e "$base.$ext";
    print  STDERR "$format\n";
    use Bio::SeqIO;
    my $in  = Bio::SeqIO->new(-file => "$base.$ext",
                              -format => $format);

    my $out  = Bio::SeqIO->new(-file => ">$base-bioperl.$ext",
                               -format => $format);
    my $seq = $in->next_seq();

    # debugging print statements here
    # print Dumper $seq if $format eq 'embl';
    print "----\n";
    print ref($seq), "\n";
    print $seq->display_id, "\n";
    print $seq->version, "\n";
    print $seq->accession_number, "\n";
    print $seq->get_secondary_accessions , "\n" if $seq->isa('Bio::RichSeqI');

    print "--------------------------------\n";
    $out->write_seq($seq);
}

