if (! this.sh_languages) {
  this.sh_languages = {};
}
sh_languages['desktop'] = [
  [
    [
      /#/g,
      'sh_comment',
      1
    ],
    [
      /\[.*\]/g,
      'sh_section',
      1
    ],
    [
      /([^="\[]+)((?:\[.+\])*)([ \t]*)(=)/g,
      ['sh_type', 'sh_paren', 'sh_normal', 'sh_symbol'],
      -1
    ]
  ],
  [
    [
      /$/g,
      null,
      -2
    ]
  ]
];
